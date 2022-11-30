Kakao.init('520d3ac30c04e74794bd2b9169b2503c');
console.log(Kakao.isInitialized());

function kakaoLogin() {
    Kakao.Auth.login({
      success:  (response) => {
        console.log("첫번쨰 success response " + response);
        Kakao.API.request({
          url: '/v2/user/me',
          success: (response) => {
            console.log(response);
        	console.log("액세스토큰" + Kakao.Auth.getAccessToken());

        	checkMember(response);

          },
          fail: (error) => { console.log(error); },
        })
      },
      fail: (error) => { console.log(error); },
  });
 }


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

function createMember(kakaoId, nickname, email) {
    var httpRequest = createXMLHttpRequest();
    var data = {
        "id": kakaoId,
        "nickname": nickname,
        "email": email
    }

    httpRequest.open('POST', '/members', true);
    httpRequest.setRequestHeader('Content-Type', 'application/json');

        httpRequest.onreadystatechange = () => {
            if (httpRequest.readyState === XMLHttpRequest.DONE) {
                // 이상 없음, 응답 받았음
                if (httpRequest.status === 200) {
                	var result = httpRequest.response;
                	console.log("createmember result" + result);
                	console.log("data sdfsdfsdfsdfdfgdfg" + data);
                	console.log("stringify " + JSON.stringify(data));
            } else {
                alert(httpRequest.status +' error. request에 뭔가 문제가 있어요.');
            }
        };
    }

    httpRequest.send(JSON.stringify(data));
}

function checkMember(response) {
    var httpRequest = createXMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            // 이상 없음, 응답 받았음
            if (httpRequest.status === 200) {
                console.log("httpRequest.responsehttpRequest.response" + httpRequest.response);
                var result = httpRequest.response;
                if(result == "join") {
                    console.log("join");
                    console.log(response);
                    console.log(response.id, response.properties.nickname, response.kakao_account.email);
                    createMember(response.id, response.properties.nickname, response.kakao_account.email);
                } else {
                    console.log("exist");
                }
            } else {
            alert(httpRequest.status +' error. request에 뭔가 문제가 있어요.');
            }
        };
    }
    httpRequest.open('GET', '/members/'+response.id, true);
    httpRequest.send();
}


function kakaoLogout() {
    if(!Kakao.Auth.getAccessToken()) {
        console.log(Kakao.Auth.getAccessToken());
        return;
    }else{
        Kakao.Auth.logout();
        console.log(Kakao.Auth.getAccessToken());
    }
}
