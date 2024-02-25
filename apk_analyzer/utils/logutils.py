import logging
import sys


def get_logger(logger_name):
    logger = logging.getLogger(logger_name)
    stdout_handler = logging.StreamHandler(sys.stdout)
    formatter = logging.Formatter('%(levelname)s:  %(asctime)s - %(name)s - %(message)s')
    stdout_handler.setFormatter(formatter)
    logger.addHandler(stdout_handler)
    logger.setLevel(logging.DEBUG)
    return logger
