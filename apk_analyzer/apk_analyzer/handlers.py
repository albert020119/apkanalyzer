from fastapi import APIRouter, UploadFile, BackgroundTasks

from apk_analyzer import ApkAnalyzer
from apk_analyzer.dto import StartAnalysis, AnalysisStatus
from utils.logutils import get_logger

from .utils import download_file, calc_md5

log = get_logger("apk_analyer")
router = APIRouter()
analyzer = ApkAnalyzer(logger=log)


@router.get("/")
async def root():
    return {"message": "Hello World"}


@router.post("/apk", response_model=StartAnalysis)
async def analyze_apk(file: UploadFile, background_tasks: BackgroundTasks):
    downloaded_file_path = download_file(file)
    checksum = calc_md5(downloaded_file_path)
    analyzer.analyzer_db.add_new(checksum, downloaded_file_path)
    background_tasks.add_task(analyzer.analyze, downloaded_file_path, checksum)
    return StartAnalysis(md5=checksum, filename=file.filename)


@router.get("/status", response_model=AnalysisStatus)
async def update_status(md5: str):
    result = await analyzer.get_status(md5)
    if not result:
        return {}
    return result
