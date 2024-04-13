import requests
import json

from base64 import urlsafe_b64encode


class UrlScanner:
    virus_total_api: str = "https://www.virustotal.com/api/v3/urls/"
    headers = {
        "accept": "application/json",
        "content-type": "application/x-www-form-urlencoded",
        "x-apikey": "c10830acb2201a4c500f7cb48e86500135ca971f5edebccced9ef19e1ce07166"
    }

    @staticmethod
    def scan_url(url: str) -> bool:
        """
        returns true if url is malicious \n
        url is considered malicious if more than 5 avs scanned it as malicious
        """
        url_id = urlsafe_b64encode(url.encode()).decode().strip("=")
        try:
            response = requests.get("{}/{}".format(UrlScanner.virus_total_api, url_id), headers=UrlScanner.headers)
            data = json.loads(response.content)
            last_analysis_result = data.get("data").get("attributes").get("last_analysis_stats")
            if last_analysis_result.get("malicious") >= 5:
                return True
            return False
        except Exception as e:
            print(e)
            return False
