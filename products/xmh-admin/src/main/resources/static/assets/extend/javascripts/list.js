/**
 * @author Xu minghua 2017/7/18
 */
jQuery(document).ready(function () {
    /*<![CDATA[*/

    var $listForm = $("#listForm");
    var $pageTotal = $("#pageTotal");
    var $deleteButton = $("#deleteButton");
    var $listTable = $("#listTable");
    var $selectAll = $("#selectAll");
    var $ids = $("#listTable input[name='ids']");
    var $contentRow = $("#listTable tr:gt(0)");
    var $pageSizeOption = $("#pageSizeOption a");
    var $pageSize = $("#pageSize");
    var $pageNumber = $("#pageNumber");

    // 全选
    $selectAll.click(function () {
        var $this = $(this);
        var $enabledIds = $("#listTable input[name='ids']:enabled");
        if ($this.prop("checked")) {
            $enabledIds.parent().addClass("checked");
            $enabledIds.prop("checked", true);
            if ($enabledIds.filter(":checked").size() > 0) {
                $deleteButton.removeAttr("disabled");
                $contentRow.addClass("active");
            } else {
                $deleteButton.prop("disabled", true);
            }
        } else {
            $enabledIds.parent().removeClass("checked");
            $enabledIds.prop("checked", false);
            $deleteButton.prop("disabled", true);
            $contentRow.removeClass("active");
        }
    });

    // 选择
    $ids.click(function () {
        var $this = $(this);
        if ($this.prop("checked")) {
            $this.closest("tr").addClass("active");
            $deleteButton.removeAttr("disabled");
        } else {
            $this.closest("tr").removeClass("active");
            if ($("#listTable input[name='ids']:enabled:checked").size() > 0) {
                $deleteButton.removeAttr("disabled");
            } else {
                $deleteButton.prop("disabled", true);
            }
        }
    });

    // 删除
    $deleteButton.click(function () {
        var $this = $(this);
        if ($this.attr("disabled")) {
            return false;
        }
        var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
        bootbox.confirm("您确定要删除吗!", function (result) {
            if (result) {
                $.ajax({
                    url: "delete",
                    type: "POST",
                    data: $checkedIds.serialize(),
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        $.message(message);
                        if (message.type == "success") {
                            $pageTotal.text(parseInt($pageTotal.text()) - $checkedIds.size());
                            $checkedIds.closest("tr").remove();
                            // if ($listTable.find("tr").size() <= 1) {
                            setTimeout(function () {
                                location.reload(true);
                            }, 3000);
                            // }
                        }
                        $deleteButton.attr("disabled", true);
                        $selectAll.prop("checked", false);
                        $checkedIds.prop("checked", false);
                    }
                });
            }
        });
    });

    // 每页记录数
    $pageSizeOption.click(function () {
        var $this = $(this);
        $pageSize.val($this.attr("val"));
        $pageNumber.val("0");
        $listForm.submit();
        return false;
    });

    // 页码输入
    $pageNumber.keypress(function (event) {
        var key = event.keyCode ? event.keyCode : event.which;
        if ((key == 13 && $(this).val().length > 0) || (key >= 48 && key <= 57)) {
            return true;
        } else {
            return false;
        }
    });

    // 表单提交
    $listForm.submit(function () {
        if (!/^\d*[1-9]\d*$/.test($pageNumber.val())) {
            $pageNumber.val("1");
        }
        $pageNumber.val($pageNumber.val() - 1);
    });

    // 页码跳转
    $.pageSkip = function (_this) {
        $pageNumber.val($(_this).attr("pageIndex"));
        $listForm.submit();
        return false;
    };

    /*]]>*/
});
