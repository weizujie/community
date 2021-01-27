// 页面加载完之后初始化这个按钮，增加一个点击事件
$(function () {
    $("#publishBtn").click(publish);
});

function publish() {
    // 显示发帖框
    $("#publishModal").modal("hide");

    // 获取标题和内容
    var title = $("#recipient-name").val();
    var content = $("#message-text").val();
    if (title !== "" && content !== "") {
        // 发送异步请求
        $.post(
            "/post/add", // url
            {"title": title, "content": content}, // data
            function (data) { // 这个 data 是后台传过来的数据（String）
                data = $.parseJSON(data); // 转换为 json 对象
                // 在提示框中显示返回消息
                $("#hintBody").text(data.msg);
                // 显示提示框
                $("#hintModal").modal("show");
                // 2 秒后隐藏
                setTimeout(function () {
                    $("#hintModal").modal("hide");
                    // 刷新页面
                    if (data.code === 0) {
                        window.location.reload();
                    }
                }, 2000);
            }
        );
    } else {
        alert("标题或内容不能为空!");
    }

}