from fastapi import APIRouter, UploadFile

from apk_analyzer import ApkAnalyzer
from apk_analyzer.dto import StartAnalysis, AnalysisStatus
from utils.logutils import get_logger

log = get_logger("apk_analyer")
router = APIRouter()
analyzer = ApkAnalyzer(logger=log)


@router.get("/")
async def root():
    return {"message": "Hello World"}


@router.post("/apk", response_model=StartAnalysis)
async def analyze_apk(file: UploadFile):
    log.info("received file: {}".format(file.filename))
    result = analyzer.start_analysis(file)
    return result

@router.post("/apk", response_model=AnalysisStatus)
async def update_status(md5: str):
    result = analyzer.get_status(md5)
    return result
