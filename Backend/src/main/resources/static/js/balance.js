function getBalance(){
    $.get({
        url: "/dashboard/getbal",
        dataType:"json",
        success: function (result) {
            $("#resultBal").html(result["response"]);
            $('#resultTime').html(result["date"]);
        }
    });
}
getBalance();
setInterval(getBalance, 10000);