import uvicorn as uvicorn

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from apk_analyzer import handlers


def build_app():
    app = FastAPI()
    app.add_middleware(
        CORSMiddleware,
        allow_origins=['http://localhost:3000'],
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

    app.include_router(handlers.router)
    return app


if __name__ == "__main__":
    application = build_app()
    uvicorn.run(application, host="0.0.0.0", port=8000)
