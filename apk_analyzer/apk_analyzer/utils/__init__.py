import os
import shutil
import hashlib

from fastapi import UploadFile


def download_file(upload_file: UploadFile) -> str:
    if not os.path.exists("files"):
        os.mkdir("files")
    path = f"files/{upload_file.filename}"
    with open(path, 'w+b') as file:
        shutil.copyfileobj(upload_file.file, file)
    return path


def calc_md5(file) -> str:
    md5 = hashlib.md5(open(file, 'rb').read()).hexdigest()
    return md5
