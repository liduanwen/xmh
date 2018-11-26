/* 音频 */
var recorder;
var audio = document.querySelector("audio");
var playback = document.getElementById("playback");

//开始录音
function startRecording() {
    playback.className = "hidden";
    HZRecorder.get(function (rec) {
        recorder = rec;
        recorder.start();
    });
}

//获取录音
function obtainRecord() {
    var record = recorder.getBlob();
    var formData = new FormData();
    //console.log($FileOrBlob);
    formData.append("file", record);
    $.ajax({
        url: "/common/upload",
        type: "POST",
        data: formData,
        // dataType: "json",
        cache: false,
        processData: false,
        contentType: false,
        success: function (message) {
            $.message(message);
            if (message.type == "success") {
                // $pageTotal.text(parseInt($pageTotal.text()) - $checkedIds.size());
                // $checkedIds.closest("tr").remove();
                // // if ($listTable.find("tr").size() <= 1) {
                // setTimeout(function() {
                //     location.reload(true);
                // }, 3000);
                // // }
            }
            // $deleteButton.attr("disabled", true);
            // $selectAll.prop("checked", false);
            // $checkedIds.prop("checked", false);
        }
    });
    // debugger;
}

//停止录音
function stopRecord() {
    playback.className = "col-md-4 input";
    audio.src = window.URL.createObjectURL(recorder.getBlob());
    recorder.stop();
}

//播放录音
function playRecord() {
    recorder.play(audio);
}

/* 视频 */
function scamera() {
    var videoElement = document.getElementById('video1');
    var canvasObj = document.getElementById('canvas1');
    var context1 = canvasObj.getContext('2d');
    context1.fillStyle = "#ffffff";
    context1.fillRect(0, 0, 320, 240);
    context1.drawImage(videoElement, 0, 0, 320, 240);
}

function playVideo() {
    var videoElement1 = document.getElementById('video1');
    var videoElement2 = document.getElementById('video2');
    videoElement2.setAttribute("src", videoElement1);
}