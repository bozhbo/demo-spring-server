
var stompClient = null;

function init() {
	disconnect();
	connectToServer();
}

function connectToServer() {
	var socket = new SockJS('/endpointSang');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected:' + frame);
        stompClient.subscribe('/topic/getResponse', callback);
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log('Disconnected');
}

function setConnected(connected) {

}

function callback(value) {
	alert(value);
}

function backLogin() {
	$("#backForm").submit();
}
