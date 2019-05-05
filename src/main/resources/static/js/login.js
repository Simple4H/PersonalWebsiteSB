$(document).ready(function () {
    $.ajax({
        type        : 'post',
        url         : 'http://localhost:7777/user/get_user_info.do',
        contentType : 'application/json;charset=utf-8',
        dataType    : 'json',
        data        :  data
    })
});