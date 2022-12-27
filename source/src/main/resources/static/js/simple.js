"use strict";

function get( url, callback, async ) {
    let httpRequest = new XMLHttpRequest();
    let response;

    if (!httpRequest) {
        console.error("Browser Not Supported.");
        return false;
    }

    httpRequest.onreadystatechange = () => {
        try {
            if (httpRequest.readyState === XMLHttpRequest.DONE) {
                if (httpRequest.status === 200) {
                    try {
                        response = JSON.parse(httpRequest.response);
                    } catch (e) {
                        response = httpRequest.response;
                    } finally { callback(response); }
                } else {
                    console.warn("url : " + url);
                    console.warn("httpRequest.status : " + httpRequest.status.toString());
                    return false;
                }
            }
        } catch (e) {
            console.error("Caught Exception : " + e.description);
        }
    }
    httpRequest.open("GET", url, async);
    httpRequest.send();
}

function cursorToEnd( element ) {
    if (element instanceof HTMLInputElement) {
        element.selectionStart = element.selectionEnd = element.value.length;
    } else console.error("Not supported element.");
}
