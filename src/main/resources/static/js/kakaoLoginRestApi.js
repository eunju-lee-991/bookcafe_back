
function createXMLHttpRequest() {
    var httpRequest;

    if (window.XMLHttpRequest) { // 모질라, 사파리, IE7+ ...
        httpRequest = new XMLHttpRequest();
    }

    if(!httpRequest) {
      alert('XMLHTTP 인스턴스를 만들 수가 없어요 ㅠㅠ');
      return false;
    }else{
        return httpRequest;
    }
}

function kakaoLoginRestApi() {

    //var httpRequest = createXMLHttpRequest();

    var restApiKey = "7bcfc7029ccb017c031af94a6e2dd46a";
    var redirectUrl = "http://localhost:8080/api/token";

    var url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+restApiKey+"&redirect_uri="+redirectUrl;

    location.href = url;


//
//    httpRequest.onreadystatechange = () => {
//        if (httpRequest.readyState === XMLHttpRequest.DONE) {
//            // 이상 없음, 응답 받았음
//            if (httpRequest.status === 200) {
//                var result = httpRequest.response;
//                console.log(result);
//            }
//        }
//    }
//
//    httpRequest.open('GET', "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+restApiKey+"&redirect_uri="+redirectUrl
//                    , true);
//    httpRequest.send();

}