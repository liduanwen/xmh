/**
 * @author Xu minghua 2017/7/18
 */

function uploadFile(form, file) {
    $.ajax({
        url: "/uploadFile",
        type: "POST",
        data: new FormData($("#" + form)[0]),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            $("#" + file).val(data);
            $.message("success", "上传成功");
        },
        error: function () {
        }
    });
}
