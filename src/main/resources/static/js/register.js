$(function () {
    $("input").focus(clear_error);
});

function clear_error() {
    $(this).removeClass("is-invalid");
}