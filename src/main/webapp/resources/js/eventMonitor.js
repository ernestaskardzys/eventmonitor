var stompClient = null;

function setConnected() {
    document.getElementById('response').innerHTML = '';
}

function connect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }

    var socket = new SockJS(contextPath + '/events');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected();
        console.log('Connected: ' + frame);
        stompClient.subscribe('/server/list', function (message) {
            showResponse(JSON.parse(message.body).data.content);
        });

        stompClient.send("/eventmonitor/events", {}, JSON.stringify({}));
    }, reconnect);
}

function reconnect() {
    // If application is disconnected - permanently try to reconnect
    setTimeout(connect, 10000);
    console.log('Reconecting in 10 seconds');
}

function showResponse(message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message));
    response.appendChild(p);
}
