    /************************************************************
     * 전역변수
     ************************************************************/
    let select01;
    let select02;
    const chkAll = document.getElementById('chkAll');
    const aprvYn = document.getElementById('aprvYn')
    const grid01 = document.getElementById('grid01');
    const grid02 = document.getElementById('grid02');
    const grid03 = document.getElementById('grid03');

    /************************************************************
     * function
     ************************************************************/    
    /*
     * @Desc
     * 메타공통코드조회
     * 메타에서 사용하는 공통코드를 조회한다.
     */
    const fn_selectListMetaCommonCd = () => {
        fn_getAjax('/meta/selectListMetaCommonCd', "", fn_selectListMetaCommonCdCallback);
    }

    /*
     * @Desc
     * 메타공통코드조회콜백
     * 조회된 공통코드를 option 태그로 생성해 전역변수에 저장한다.
     */
    const fn_selectListMetaCommonCdCallback = (res) => {

        let innerHTML = "";
        res.dataType.forEach(item => 
            innerHTML += "<option value='" + item + "'>" + item + "</option>"
        );
        select01 = innerHTML;

        innerHTML = "";
        res.Type.forEach(item => 
            innerHTML += "<option value='" + item + "'>" + item + "</option>"
        );
        select02 = innerHTML;

        // 1줄추가
        fn_changeLine('addBtn01');
        fn_changeFieldLine('addBtn02');
    }

    /*
     * @Desc
     * 메타동기화
     * 생성된 테이블들의 필드명을 메타용어로 동기화한다.
     */
    const fn_syncronizeMetaTerm = () => {
        fn_getAjax('/meta/synchronizeMetaTerm', "", fn_syncronizeMetaTermCallback);
    }

    /*
     * @Desc
     * 메타동기화콜백
     * 메타동기화에 성공하면 페이지를 새로고침한다.
     */
    const fn_syncronizeMetaTermCallback = (res) => {
        window.location.reload();
    }

    /*
     * @Desc
     * 일괄결재
     * 결재를 받지 못한 용어데이터들을 일괄결재한다.
     */
    const fn_updateApprovalAllMetaTerm = () => {
        fn_getAjax('/meta/updateApprovalAllMetaTerm', "", fn_updateApprovalAllMetaTermCallback);
    }

    /*
     * @Desc
     * 일괄결재콜백
     * 일괄결재에 성공하면 페이지를 새로고침한다.
     */
    const fn_updateApprovalAllMetaTermCallback = (res) => {
        window.location.reload();
    }

    /*
     * @Desc
     * 메타용어다건조회
     * 결재여부를 조건으로 메타에 등록된 데이터들을 다건조회한다.
     */
    const fn_selectListTmMetaTerm = () => {
        aprvYnValue = aprvYn.value;
        const data = {aprvYn: aprvYnValue};
        fn_getAjax('/meta/selectListTmMetaTerm', data, fn_selectListMetaTermCallback);
    }

    /*
     * @Desc
     * 메타용어다건조회콜백
     * 조회된 메타용어정보를 row로 만들어 화면에 표시한다.
     */
    const fn_selectListMetaTermCallback = (res) => {
        for (let i = grid01.rows.length-1; i > 0; i--)
            grid01.deleteRow(i);

        res.forEach(item => {
            const newRow = grid01.insertRow();
            const td00 = newRow.insertCell(0);
            const td01 = newRow.insertCell(1);
            const td02 = newRow.insertCell(2);
            const td03 = newRow.insertCell(3);
            const td04 = newRow.insertCell(4);
            const td05 = newRow.insertCell(5);
            const td06 = newRow.insertCell(6);
            const td07 = newRow.insertCell(7);
            const td08 = newRow.insertCell(8);

            const input00 = document.createElement('input');
            input00.setAttribute('type', 'checkbox');
            input00.setAttribute('class', 'chk');
            input00.setAttribute('value', item.termSeq);

            const input01 = document.createElement('input');
            input01.setAttribute('type', 'text');
            input01.setAttribute('class', '');
            input01.setAttribute('value', item.phyNm);
            input01.setAttribute('readonly', 'readonly');

            const input02 = document.createElement('input');
            input02.setAttribute('type', 'text');
            input02.setAttribute('class', '');
            input02.setAttribute('value', item.dataType);
            input02.setAttribute('readonly', 'readonly');

            const input03 = document.createElement('input');
            input03.setAttribute('type', 'text');
            input03.setAttribute('class', '');
            input03.setAttribute('value', item.logNm);
            input03.setAttribute('readonly', 'readonly');

            const input04 = document.createElement('input');
            input04.setAttribute('type', 'text');
            input04.setAttribute('class', '');
            input04.setAttribute('value', item.termSeq);
            input04.setAttribute('readonly', 'readonly');

            const input05 = document.createElement('input');
            input05.setAttribute('type', 'text');
            input05.setAttribute('class', '');
            input05.setAttribute('value', item.dataLength);
            input05.setAttribute('readonly', 'readonly');

            const input06 = document.createElement('input');
            input06.setAttribute('type', 'text');
            input06.setAttribute('class', '');
            input06.setAttribute('value', item.dataDecimal);
            input06.setAttribute('readonly', 'readonly');

            const input07 = document.createElement('input');
            input07.setAttribute('type', 'text');
            input07.setAttribute('class', '');
            input07.setAttribute('value', item.aprvYn);
            input07.setAttribute('readonly', 'readonly');

            const input08 = document.createElement('input');
            input08.setAttribute('type', 'text');
            input08.setAttribute('class', '');
            input08.setAttribute('value', item.aprvYn);
            input08.setAttribute('readonly', 'readonly');

            td00.appendChild(input00);
            td01.appendChild(input01);
            td02.appendChild(input02);
            td03.appendChild(input03);
            td04.appendChild(input04);
            td05.appendChild(input05);
            td06.appendChild(input06);
            td07.appendChild(input07);
            td08.appendChild(input08);
        });
    }

    /*
     * @Desc
     * 메타용어다건삭제
     * 체크박스를 선택하면 용어일련번호(termSeq)를 배열로 만들어 등록된 메타용어를 다건삭제한다.
     */
    const fn_deleteListTmMetaTerm = () => {
        if (!confirm("선택하신 메타용어를 삭제하시겠습니까?"))
            return;

        let chkArr = new Array();
        const chkList = document.getElementsByClassName('chk');
        for (let i = 0; i < chkList.length; i++) {
            if (chkList[i].checked == true) {
                chkArr.push(chkList[i].value);
            }
        }

        const data = {chkArr: chkArr};
        fn_postAjax('/meta/deleteListTmMetaTerm', data, fn_deleteListTmMetaTermCallback);
    }

    /*
     * @Desc
     * 메타용어다건삭제콜백
     * 메타용어다건삭제에 성공하면 페이지를 새로고침한다.
     */
    const fn_deleteListTmMetaTermCallback = (res) => {
        window.location.reload();
    }

    /*
     * @Desc
     * 메타용어등록테이블로우변경
     * 메타용어를 등록을 할 수 있는 테이블 로우를 한 줄 추가하거나 삭제한다.
     */
    const fn_changeLine = (id) => {

        if (id == 'addBtn01') {
            const rowIndex = grid02.tBodies[0].rows.length;

            const newRow = grid02.tBodies[0].insertRow();
            const newCell0 = newRow.insertCell(0);
            const newCell1 = newRow.insertCell(1);
            const newCell2 = newRow.insertCell(2);
            const newCell3 = newRow.insertCell(3);
            const newCell4 = newRow.insertCell(4);

            newCell0.innerHTML = "<td><input type='text' class='' id='' name='listEntity[" + rowIndex + "].phyNm' /></td>";
            newCell1.innerHTML = "<td><select class='' id='' name='listEntity[" + rowIndex + "].dataType' />" + select01 + "</td>";
            newCell2.innerHTML = "<td><input type='text' class='' id='' name='listEntity[" + rowIndex + "].logNm' /></td>";
            newCell3.innerHTML = "<td><input type='text' class='' id='' name='listEntity[" + rowIndex + "].dataLength' value='0' /></td>";
            newCell4.innerHTML = "<td><input type='text' class='' id='' name='listEntity[" + rowIndex + "].dataDecimal' value='0' /></td>";
        } else if (id == 'delBtn01') {
            if (grid02.rows.length == 2) return;
            grid02.deleteRow(grid02.rows.length - 1);
        }
    }

    /*
     * @Desc
     * 메타용어데이터다건등록
     * 메타용어데이터를 row갯수만큼 다건등록한다.
     */
    const fn_insertMetaTerm = () => {
        const data = $('#frm01').serialize();
        fn_postAjax('/meta/insertListMetaWord', data, fn_insertMetaTermCallback)
    }

    /*
     * @Desc
     * 메타용어데이터다건등록콜백
     * 메타용어데이터다건등록에 성공하면 페이지를 새로고침한다.
     */
    const fn_insertMetaTermCallback = () => {
        window.location.reload();
    }

    /*
     * @Desc
     * 테이블생성테이블로우변경
     * 테이블을 생성할 수 있는 테이블 로우를 한 줄 추가하거나 삭제한다.
     */
    const fn_changeFieldLine = (id) => {
        if (id == 'addBtn02') {
            const rowIndex = grid03.tBodies[0].rows.length;

            const newRow = grid03.tBodies[0].insertRow();
            const newCell0 = newRow.insertCell(0);
            const newCell1 = newRow.insertCell(1);
            const newCell2 = newRow.insertCell(2);
            const newCell3 = newRow.insertCell(3);
            const newCell4 = newRow.insertCell(4);
            const newCell5 = newRow.insertCell(5);

            newCell0.innerHTML = "<td><input type='text' class='' id='' name='fieldListEntity[" + rowIndex + "].Field' /></td>";
            newCell1.innerHTML = "<td><select class='' id='' name='fieldListEntity[" + rowIndex + "].Type' />" + select02 + "</td>";
            newCell2.innerHTML = "<td><input type='number' class='' id='' name='fieldListEntity[" + rowIndex + "].Length' value='0' /></td>";
            newCell3.innerHTML = "<td><input type='text' class='' id='' name='fieldListEntity[" + rowIndex + "].Comment' /></td>";
            newCell4.innerHTML = "<td><input type='checkbox' class='' id='' name='fieldListEntity[" + rowIndex + "].NotNull' /></td>";
            newCell5.innerHTML = "<td><input type='checkbox' class='' id='' name='fieldListEntity[" + rowIndex + "].PrimaryKey' /></td>";
        } else if (id == 'delBtn02') {
            if (grid03.rows.length == 2) return;
            grid03.deleteRow(grid03.rows.length - 1);
        }
    }

    /*
     * @Desc
     * 테이블생성
     * 테이블을 생성한다.
     */
    const fn_createTable = () => {
        const data = $('#frm02').serialize();
        fn_postAjax('/meta/createTable', data, fn_createTableCallback);
    }

    /*
     * @Desc
     * 테이블생성콜백
     * 테이블 생성에 성공하면 페이지를 새로고침한다.
     */
    const fn_createTableCallback = () => {
        window.location.reload();
    }

    
    /************************************************************
     * addEventListener
     ************************************************************/
    /*
     * 전체체크
     * 체크박스를 모두 체크하거나 모두 체크해제한다.
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