$(function () {
    $("#sendBtn").click(send_letter);
    $(".close").click(delete_msg);
});

function send_letter() {
    // 显示发私信框
    $("#sendModal").modal("hide");

    // 获取目标用户和内容
    var toName = $("#recipient-name").val();
    var content = $("#message-text").val();

    if (toName !== "" && content !== "") {
        // 发送异步请求
        $.post(
            "/letter/send",
            {"toName": toName, "content": content},
            function (data) {
                data = $.parseJSON(data);
                $("#hintModal").modal("show");
                if (data.code === 0) {
                    $("#hintBody").text("发送成功!");
                } else {
                    $("#hintBody").text(data.msg);
                }
                setTimeout(function () {
                    $("#hintModal").modal("hide");
                    location.reload();
                }, 2000);
            }
        );
    } else {
        alert("目标用户或内容不能为空!");
    }

}

function delete_msg() {
    // TODO 删除数据
    $(this).parents(".media").remove();
}