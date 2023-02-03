'use strict';

function get( url, callback, async ) {
    let httpRequest = new XMLHttpRequest();
    let response;

    if (!httpRequest) {
        console.error('Browser Not Supported.');
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
                    console.warn('url : ' + url);
                    console.warn('httpRequest.status : ' + httpRequest.status.toString());
                    return false;
                }
            }
        } catch (e) {
            console.error('Caught Exception : ' + e.description);
        }
    }
    httpRequest.open('GET', url, async);
    httpRequest.send();
}

/** Caution : Use only fetch from own site. */
function fetchJson( url, json, callback ) {
    let options = {
        method : 'POST',
        credentials : 'include',
        headers : {
            'Content-Type' : 'application/json',
            'Accept' : 'application/json'
        },
        body : JSON.stringify(json)
    };

    let csrf_name = document.getElementsByName('_csrf_header');
    let csrf_value = document.getElementsByName('_csrf');
    if (csrf_name.length > 0) options.headers[csrf_name[0].content] = csrf_value[0].content;


    fetch(url, options)
        .then((response) => response.json())
        .then((data) => callback(data));
}

function cursorToEnd( element ) {
    if (element instanceof HTMLInputElement) {
        element.selectionStart = element.selectionEnd = element.value.length;
    } else console.error('Not supported element.');
}
