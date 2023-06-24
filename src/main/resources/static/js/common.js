    /************************************************************
     * 전역변수
     ************************************************************/

    /************************************************************
     * AJAX
     ************************************************************/
    /*
     * AJAX 비동기통신
     * GET타입 AJAX 비동기통신
     */
    const fn_getAjax = (url, data, callback) => {
        $.ajax({
            url: url,
            type: 'GET',
            data: data,
            success: function(res) {
                callback(res);
            },
            error: function(e) {
                alert("ERROR: ", e);
            }
        });
    }

    /*
     * AJAX 비동기통신
     * POST타입 AJAX 비동기통신
     */
    fn_postAjax = (url, data, callback) => {
        $.ajax({
            url: url,
            type: 'POST',
            data: data,
            success: function(res) {
                callback(res);
            },
            error: function(e) {
                alert("ERROR: ", e);
            }
        });
    }


    /************************************************************
     * function
     ************************************************************/
    /*
     * 탭이동
     * 탭을 누르면 해당 탭에 맞는 페이지를 보여준다
     */
    const fn_clickTabMenu = (tabBtn) => {
        let tabId;
        switch(tabBtn) {
            case "tabBtn1": tabId = "tab1"; break;
            case "tabBtn2": tabId = "tab2"; break;
            case "tabBtn3": tabId = "tab3"; break;
        }
        let tabList = document.getElementsByClassName("tab");
        for (var i = 0; i < tabList.length; i++) {
            tabList[i].classList.add("off");
        }
        document.getElementById(tabId).classList.remove("off");
    }

    /************************************************************
     * addEventListener
     ************************************************************/
    /*
     * 선택삭제
     * 체크박스로 선택해서 메타에 등록된 데이터들을 선택삭제한다.
     */
    chkAll.addEventListener('click', function(event) {
        const chkList = document.getElementsByClassName('chk');
        if(chkAll.checked) {
            for (let i = 0; i < chkList.length; i++)
                chkList[i].checked = true;
        } else {
            for (let i = 0; i < chkList.length; i++)
                chkList[i].checked = false;
        }
    })