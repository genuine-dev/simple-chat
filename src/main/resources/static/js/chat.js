
/**
 * 初期化処理
 */
var ChatStomp = function ( endpointPath ) {

    this.simpleChatSocketUrl = location.host + endpointPath; // チャット用SocketエンドポイントURL
    this.name = "";// ユーザー名
    this.userName = document.getElementById('name'); // ユーザー名入力フォーム
    this.connectButton = document.getElementById('connect'); // 参加するボタン
    this.disconnectButton = document.getElementById('disconnect'); //退室するボタン
    this.inputArea = document.getElementById('inputArea')
    this.sendButton = document.getElementById('send'); //

    // イベントハンドラの登録
    this.connectButton.addEventListener('click', this.connect.bind(this));
    this.disconnectButton.addEventListener('click', this.disconnect.bind(this));
    this.sendButton.addEventListener('click', this.sendMessage.bind(this));
};

/**
 * エンドポイントへの接続処理
 */
ChatStomp.prototype.connect = function () {
    var socket = new WebSocket("ws://"+ this.simpleChatSocketUrl);
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, this.onConnected.bind(this));
};

/**
 * エンドポイントへ接続したときの処理
 */
ChatStomp.prototype.onConnected = function (frame) {
    console.log('Connected: ' + frame);
    this.stompClient.subscribe('/topic/messages', this.onSubscribeMessage.bind(this));
    this.setConnected(true);
    this.name = document.getElementById('name').value=="" ? '名無し': document.getElementById('name').value ;
    this.stompClient.send('/app/message', {}, this.name + 'さんが参加しました。');
};

/**
 * '/topic/messages'メッセージを受信したときの処理
 */
ChatStomp.prototype.onSubscribeMessage = function (message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.appendChild(document.createTextNode(JSON.parse(message.body).value));
    response.insertBefore(p, response.children[0]);
};

/**
 * '/app/message'へメッセージ送信
 */
ChatStomp.prototype.sendMessage = function () {
    var message = document.getElementById('message').value;
    this.stompClient.send('/app/message', {}, this.name + '>' + message);
    document.getElementById('message').value="";
};

/**
 * 切断
 */
ChatStomp.prototype.disconnect = function () {

    this.stompClient.send('/app/message', {}, this.name +'さんが退室しました。');

    if (this.stompClient) {
        this.stompClient.disconnect();
        this.stompClient = null;
    }
    this.setConnected(false);
};

/**
 * ボタン表示の切り替え
 */
ChatStomp.prototype.setConnected = function (connected) {
    this.userName.disabled = connected;
    this.connectButton.disabled = connected;
    this.disconnectButton.disabled = !connected;
    this.inputArea.style.display = connected  ? 'block' : 'none';
};

