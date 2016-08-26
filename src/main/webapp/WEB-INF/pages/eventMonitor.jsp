<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Event Monitor</title>
        <script>var contextPath = "${pageContext.request.contextPath}"</script>
        <script src="resources/js/sockjs-0.3.4.js"></script>
        <script src="resources/js/stomp.js"></script>
        <script src="resources/js/eventMonitor.js"></script>
    </head>
    <body onload="connect()">
        <noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being enabled. Please enable
            Javascript and reload this page!</h2></noscript>
        <div>
            <div id="conversationDiv">
                <p id="response"></p>
            </div>
        </div>
    </body>
</html>
