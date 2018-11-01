$(document).ready(function () {
    $.ajax({
        type        : 'post',
        url         : 'http://www.test.com/user/get_user_info.do',
        contentType : 'application/json;charset=utf-8',
        dataType    : 'json',
        data        :  data
    })
});