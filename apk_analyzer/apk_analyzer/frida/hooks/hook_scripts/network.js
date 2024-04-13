  var URL = Java.use("java.net.URL");
  var HttpURLConnection = Java.use("java.net.HttpURLConnection");
  var OutputStreamWriter = Java.use("java.io.OutputStreamWriter");
  var BufferedReader = Java.use("java.io.BufferedReader");
  var webview = Java.use("android.webkit.WebView")

  webview.loadUrl.overload('java.lang.String').implementation = function (url) {
    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'network',
        method: 'webview.loadUrl',
        code: 1,
        timestamp: timestamp,
        args: [url],
        return_value: null
    };
    send(JSON.stringify(hookEventData));
    return this.loadUrl(url)
  }
  // Intercept the request
  HttpURLConnection.getOutputStream.implementation = function() {
	var outputStream = this.getOutputStream();
	var requestMethod = this.getRequestMethod();
	var url = this.getURL().toString();

    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'network',
        method: 'HttpURLConnection.getOutputStream',
        code: 1,
        timestamp: timestamp,
        args: [url],
        return_value: null
    };
    send(JSON.stringify(hookEventData));

	return outputStream;
  };
 
  // Intercept the response
  HttpURLConnection.getInputStream.implementation = function() {
	var inputStream = this.getInputStream();
	var url = this.getURL().toString();

	var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'network',
        method: 'HttpURLConnection.getInputStream',
        code: 1,
        timestamp: timestamp,
        args: [url],
        return_value: null
    };
    send(JSON.stringify(hookEventData));

	return inputStream;
  };