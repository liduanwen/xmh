var messages = {
    "info": "info",//常规消息提示，默认背景为浅蓝色
    "success": "success",//成功消息提示，默认背景为浅绿色
    "warning": "warning",//警告消息提示，默认背景为橘黄色
    "error": "error"//错误消息提示，默认背景为浅红色
};

$.message = function () {

    var message = {};
    if ($.isPlainObject(arguments[0])) {
        message = arguments[0];
    } else if (typeof arguments[0] === "string" && typeof arguments[1] === "string") {
        message.type = arguments[0];
        message.content = arguments[1];
        message.title = "";
        if (typeof arguments[2] === "string") {
            message.title = arguments[2];
        }
    } else {
        return false;
    }
    if (message.type == null || message.content == null) {
        return false;
    }
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "3000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    var $toast = toastr[message.type](message.content, message.title);
};
