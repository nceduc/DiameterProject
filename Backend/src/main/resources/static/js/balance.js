var response;
function getBalance(){
    $.get({
        url: "/dashboard/getbal",
        dataType:"json",
        success: function (result) {
            response = +result["response"];
            if(!isNaN(response)){
                response = response.toFixed(2);
            }else{
                response = result["response"];
            }
            $("#resultBal").html(response);
            $('#resultTime').html(result["date"]);
        }
    });
}
getBalance();
setInterval(getBalance, 5000);