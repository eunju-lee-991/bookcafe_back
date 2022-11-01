Kakao.init('520d3ac30c04e74794bd2b9169b2503c');
console.log(Kakao.isInitialized());

function checkMember(kakaoId) {
    var httpRequest;
    if (window.XMLHttpRequest) { // 모질라, 사파리, IE7+ ...
        httpRequest = new XMLHttpRequest();
    }
    
    if(!httpRequest) {
      alert('XMLHTTP 인스턴스를 만들 수가 없어요 ㅠㅠ');
      return false;
    }

    httpRequest.onreadystatechange = () => {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            // 이상 없음, 응답 받았음
            if (httpRequest.status === 200) {
            	var result = httpRequest.response;
            	console.log(result);
            	console.log("2222");
            	console.log(httpRequest.responseText);
        } else {
            alert(httpRequest.status +'request에 뭔가 문제가 있어요.');
        }
    };
    }

    httpRequest.open('GET', '/checkMember?id='+kakaoId, true);
    httpRequest.send();
}

function kakaoLogin() {
    Kakao.Auth.login({
      success: function (response) {
        console.log("첫번쨰 success")
        console.log(response)
        Kakao.API.request({
          url: '/v2/user/me',
          success: function (response) {
            console.log("두번쨰 success")
        	console.log(response)
        	checkMember(response.id);
          },
          fail: function (error) {
            console.log(error)
          },
        })
      },
      fail: function (error) {
        console.log(error)
      },
  });
 }
