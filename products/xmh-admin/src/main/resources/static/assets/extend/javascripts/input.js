/**
 * @author Xu minghua 2017/7/18
 */
jQuery(document).ready(function () {

    var $backButton = $("#backButton");

    //if ($.tools != null) {
    //	var $tab = $("#tab");
    //	var $title = $("#inputForm :input[title], #inputForm label[title]");
    //
    //	// Tab效果
    //	$tab.tabs("table.tabContent, div.tabContent", {
    //		tabs: "input"
    //	});
    //
    //	// 表单提示
    //	$title.tooltip({
    //		position: "center right",
    //		offset: [0, 4],
    //		effect: "fade"
    //	});
    //}

    // 返回上一页
    $backButton.click(function () {
        location.href = document.referrer;
        return false;
    });

    // 验证消息
    if ($.validator != null) {
        $.extend($.validator.messages, {
            required: "必填",
            email: "E-mail格式错误",
            url: "网址格式错误",
            date: "日期格式错误",
            dateISO: "日期格式错误",
            pointcard: "信用卡格式错误",
            number: "只允许输入数字",
            digits: "只允许输入零或正整数",
            minlength: $.validator.format("长度不允许小于{0}"),
            maxlength: $.validator.format("长度不允许大于{0}"),
            rangelength: $.validator.format("长度必须在{0}-{1}之间"),
            min: $.validator.format("不允许小于{0}"),
            max: $.validator.format("不允许大于{0}"),
            range: $.validator.format("必须在{0}-{1}之间"),
            accept: "输入后缀错误",
            equalTo: "两次输入不一致",
            remote: "已存在",
            integer: "只允许输入整数",
            positive: "只允许输入正数",
            negative: "只允许输入负数",
            decimal: "数值超出了允许范围",
            pattern: "格式错误",
            extension: "文件格式错误",
            illegal: "非法字符",
            exist: "已存在"
        });

        $.validator.setDefaults({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "",
            highlight: function (element) { // hightlight error inputs
                $(element).closest(".form-group").addClass("has-error"); // set error class to the control group
            },
            success: function (label) {
                label.closest(".form-group").removeClass("has-error");
                label.remove();
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element.closest(".input"));
            },
            submitHandler: function (form) {
                form.submit();
            }
        });
    }

});