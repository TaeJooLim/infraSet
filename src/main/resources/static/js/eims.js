    /************************************************************
     * 전역변수
     ************************************************************/
    let metaTermList;
    const input01 = document.getElementById('input01');
    const datalist01 = document.getElementById('datalist01');
    const regBtn01 = document.getElementById('regBtn01');
    const delBtn02 = document.getElementById('delBtn02');
    const depBtn02 = document.getElementById('depBtn02');
    const grid01 = document.getElementById('grid01');
    const grid02 = document.getElementById('grid02');
    const grid03 = document.getElementById('grid03');

    /************************************************************
     * function
     ************************************************************/
    /*
     * @Desc
     * 메타용어다건조회
     * 결재가 완료된 메타용어를 다건조회한다.
     */
    const fn_selectListMetaTerm = () => {
        const data = {aprvYn: 'Y'};
        fn_getAjax('/meta/selectListTmMetaTerm', data, fn_selectListMetaTermCallback);
    }

    /*
     * @Desc
     * 메타용어다건조회콜백
     * 조회된 메타용어리스트를 JSON형태로 파싱 후 전역변수에 저장한다.
     */
    const fn_selectListMetaTermCallback = (res) => {
        metaTermList = res;
    }

    /*
     * @Desc
     * 메타용어데이터리스트클릭
     * 메타용어를 검색어로 검색할 때 검색창 하위에 표시된 자동완성 그리드를 클릭하면 필드가 추가된다.
     */
    const fn_onClickDataList01 = (termSeq) => {
        datalist01.innerHTML = '';

        const rowIndex = grid01.rows.length-1;

        metaTermList.forEach((item) => {
            if (item.termSeq === termSeq) {
                const newRow = grid01.tBodies[0].insertRow();

                const newCell00 = newRow.insertCell(0);
                const newCell01 = newRow.insertCell(1);
                const newCell02 = newRow.insertCell(2);
                const newCell03 = newRow.insertCell(3);
                const newCell04 = newRow.insertCell(4);

                const input00 = document.createElement('input');
                input00.setAttribute('type', 'text');
                input00.setAttribute('class', '');
                input00.setAttribute('name', 'entityList[' + rowIndex + '].phyNm');
                input00.setAttribute('value', item.phyNm);
                input00.setAttribute('readOnly', 'readOnly');
                newCell00.appendChild(input00);
            
                const input01 = document.createElement('input');
                input01.setAttribute('type', 'text');
                input01.setAttribute('class', '');
                input01.setAttribute('name', 'entityList[' + rowIndex + '].logNm');
                input01.setAttribute('value', item.logNm);
                input01.setAttribute('readOnly', 'readOnly');
                newCell01.appendChild(input01);
                
                const input02 = document.createElement('input');
                input02.setAttribute('type', 'text');
                input02.setAttribute('class', '');
                input02.setAttribute('name', 'entityList[' + rowIndex + '].dataType');
                input02.setAttribute('value', item.dataType);
                input02.setAttribute('readOnly', 'readOnly');
                newCell02.appendChild(input02);

                const input03 = document.createElement('input');
                input03.setAttribute('type', 'text');
                input03.setAttribute('class', '');
                input03.setAttribute('name', 'entityList[' + rowIndex + '].dataLength');
                input03.setAttribute('value', item.dataLength);
                input03.setAttribute('readOnly', 'readOnly');
                newCell03.appendChild(input03);

                const input04 = document.createElement('input');
                input04.setAttribute('type', 'text');
                input04.setAttribute('class', '');
                input04.setAttribute('name', 'entityList[' + rowIndex + '].dataDecimal');
                input04.setAttribute('value', item.dataDecimal);
                input04.setAttribute('readOnly', 'readOnly');
                newCell04.appendChild(input04);
            }
        });
    }

    /*
     * @Desc
     * 엔티티생성테이블로우변경
     * 엔티티를 생성할 수 있는 컬럼 라인을 한 줄 추가하거나 삭제한다.
     */
    const fn_changeFieldLine = (id) => {
        const rowIndex = grid01.rows.length-1;
        if (id == 'addBtn01') {

            const newRow = grid01.tBodies[0].insertRow();
            const newCell00 = newRow.insertCell(0);
            const newCell01 = newRow.insertCell(1);
            const newCell02 = newRow.insertCell(2);
            const newCell03 = newRow.insertCell(3);
            const newCell04 = newRow.insertCell(4);

            const input00 = document.createElement('input');
            input00.setAttribute('type', 'text');
            input00.setAttribute('class', '');
            input00.setAttribute('name', 'entityList[' + rowIndex + '].phyNm');
            newCell00.appendChild(input00);
            
            const input01 = document.createElement('input');
            input01.setAttribute('type', 'text');
            input01.setAttribute('class', '');
            input01.setAttribute('name', 'entityList[' + rowIndex + '].logNm');
            newCell01.appendChild(input01);
            
            const input02 = document.createElement('input');
            input02.setAttribute('type', 'text');
            input02.setAttribute('class', '');
            input02.setAttribute('name', 'entityList[' + rowIndex + '].dataType');
            newCell02.appendChild(input02);

            const input03 = document.createElement('input');
            input03.setAttribute('type', 'text');
            input03.setAttribute('class', '');
            input03.setAttribute('name', 'entityList[' + rowIndex + '].dataLength');
            newCell03.appendChild(input03);

            const input04 = document.createElement('input');
            input04.setAttribute('type', 'text');
            input04.setAttribute('class', '');
            input04.setAttribute('name', 'entityList[' + rowIndex + '].dataDecimal');
            newCell04.appendChild(input04);
        } else if (id == 'delBtn01') {
            if (rowIndex == 0) return;
            grid01.deleteRow(rowIndex);
        }
    }

    /*
     * @Desc
     * 엔티티다건조회
     * 엔티티 테이블에 입력된 엔티티정보를 다건조회한다.
     */
    const fn_selectListEntity = () => {
        fn_getAjax('/eims/selectListEntity', "", fn_selectListEntityCallback);
    }

    /*
     * @Desc
     * 엔티티다건조회콜백
     * 조회된 엔티티 정보를 엔티티그리드에 표시한다.
     */
    const fn_selectListEntityCallback = (res) => {
        for (let i = grid02.rows.length-1; i > 0; i--)
            grid02.deleteRow(i);

        res.forEach(item => {
            const newRow = grid02.tBodies[0].insertRow();
            const td00 = newRow.insertCell(0);
            const td01 = newRow.insertCell(1);
            const td02 = newRow.insertCell(2);
            const td03 = newRow.insertCell(3);
            const td04 = newRow.insertCell(4);
            const td05 = newRow.insertCell(5);
            const td06 = newRow.insertCell(6);
            const td07 = newRow.insertCell(7);

            const input00 = document.createElement('input');
            input00.setAttribute('type', 'checkbox');
            input00.setAttribute('class', 'chk');
            input00.setAttribute('value', item.entityId);

            const input01 = document.createElement('input');
            input01.setAttribute('type', 'text');
            input01.setAttribute('class', '');
            input01.setAttribute('value', item.entityId);
            input01.setAttribute('readonly', 'readonly');

            const input02 = document.createElement('input');
            input02.setAttribute('type', 'text');
            input02.setAttribute('class', '');
            input02.setAttribute('value', item.entityPhyNm);
            input02.setAttribute('readonly', 'readonly');

            const input03 = document.createElement('input');
            input03.setAttribute('type', 'text');
            input03.setAttribute('class', '');
            input03.setAttribute('value', item.entityLogNm);
            input03.setAttribute('readonly', 'readonly');

            const input04 = document.createElement('input');
            input04.setAttribute('type', 'text');
            input04.setAttribute('class', '');
            input04.setAttribute('value', item.regId);
            input04.setAttribute('readonly', 'readonly');

            const input05 = document.createElement('input');
            input05.setAttribute('type', 'text');
            input05.setAttribute('class', '');
            input05.setAttribute('value', item.regDtm);
            input05.setAttribute('readonly', 'readonly');

            const input06 = document.createElement('input');
            input06.setAttribute('type', 'text');
            input06.setAttribute('class', '');
            input06.setAttribute('value', item.udtId);
            input06.setAttribute('readonly', 'readonly');

            const input07 = document.createElement('input');
            input07.setAttribute('type', 'text');
            input07.setAttribute('class', '');
            input07.setAttribute('value', item.udtDtm);
            input07.setAttribute('readonly', 'readonly');

            td00.appendChild(input00);
            td01.appendChild(input01);
            td02.appendChild(input02);
            td03.appendChild(input03);
            td04.appendChild(input04);
            td05.appendChild(input05);
            td06.appendChild(input06);
            td07.appendChild(input07);
        });
    }

    /*
     * @Desc
     * 엔티티필드단건삭제
     * 그리드 Row의 '삭제'버튼을 클릭해서 엔티티 필드를 단건 삭제한다.
     */
    const fn_deleteEntityField = (entityId, termPhyNm) => {
        if (confirm("선택하신 핃드를 삭제하시겠습니까?")) {
            const data = {entityId: entityId, termPhyNm: termPhyNm};
            fn_postAjax('/eims/deleteEntityField', data, fn_deleteEntityFieldCallback);
        }
    }

    /*
     * @Desc
     * 엔티티필드단건삭제콜백
     * 엔티티필드단건삭제에 성공하면 페이지를 새로고침한다.
     */
    const fn_deleteEntityFieldCallback = (res) => {
        window.location.reload();
    }

    /************************************************************
     * addEventListener
     ************************************************************/
    /*
     * @Desc
     * 메타용어검색자동완성
     * 메타용어를 검색어로 검색할때 검색창 하위에 자동완성이 표시된다.
     */
    input01.addEventListener('keyup', (e) => {
        const input01Value = input01.value;
        if (input01Value.trim() != '') {
            datalist01.innerHTML = '';
            metaTermList.forEach((item) => {
                if (item.phyNm.indexOf(input01Value) > -1) {
                    const div = document.createElement('div');
                    const textNode = document.createTextNode(item.phyNm);
                    div.setAttribute('class', '');
                    div.setAttribute('onClick', 'fn_onClickDataList01(' + item.termSeq + ')');
                    div.appendChild(textNode);
                    datalist01.appendChild(div);
                }
            });
        }
    })

    /*
     * @Desc
     * 엔티티등록
     * 엔티티의 물리명, 논리명, 패키지명 그리고 필드들을 받아 엔티티 테이블에 저장한다.
     */
    regBtn01.addEventListener('click', (e) => {
        fn_postAjax('/eims/insertEntity', $("#frm01").serialize(), insertEntityCallback);
    })

    /*
     * @Desc
     * 엔티티등록콜백
     * 엔티티등록에 성공하면 페이지를 새로고침한다.
     */
    const insertEntityCallback = (res) => {
        window.location.reload();
    }

    /*
     * @Desc
     * 엔티티필드다건조회
     * 엔티티의 필드 정보를 다건조회한다.
     */
    grid02.addEventListener('click', (e) => {
        const rows = grid02.rows;

        for (let i = 1; i < rows.length; i++) {
            rows[i].addEventListener('click', (e2) => {
                if (e2.target.type != 'checkbox') {
                    const rowIndex = e2.target.parentNode.parentNode.rowIndex;
                    const val = grid02.rows[rowIndex].cells[0].firstChild.value;
    
                    const data = {entityId : val};
                    fn_postAjax('/eims/selectListFieldEntity', data, fn_selectListFieldEntityCallback);
                }
            });
        }
    })

    /*
     * @Desc
     * 엔티티필드다건조회콜백
     * 조회된 엔티티필드 정보를 엔티티필드그리드에 표시한다.
     */
    const fn_selectListFieldEntityCallback = (res) => {

        for (let i = grid03.rows.length-1; i > 0; i--)
            grid03.deleteRow(i);

        res.forEach(item => {
            const newRow = grid03.tBodies[0].insertRow();
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
            input00.setAttribute('type', 'text');
            input00.setAttribute('class', '');
            input00.setAttribute('value', item.termPhyNm);
            input00.setAttribute('readonly', 'readonly');

            const input01 = document.createElement('input');
            input01.setAttribute('type', 'text');
            input01.setAttribute('class', '');
            input01.setAttribute('value', item.termLogNm);
            input01.setAttribute('readonly', 'readonly');

            const input02 = document.createElement('input');
            input02.setAttribute('type', 'text');
            input02.setAttribute('class', '');
            input02.setAttribute('value', item.termDataType);
            input02.setAttribute('readonly', 'readonly');

            const input03 = document.createElement('input');
            input03.setAttribute('type', 'text');
            input03.setAttribute('class', '');
            input03.setAttribute('value', item.entityRefType);
            input03.setAttribute('readonly', 'readonly');

            const input04 = document.createElement('input');
            input04.setAttribute('type', 'text');
            input04.setAttribute('class', '');
            input04.setAttribute('value', item.regId);
            input04.setAttribute('readonly', 'readonly');

            const input05 = document.createElement('input');
            input05.setAttribute('type', 'text');
            input05.setAttribute('class', '');
            input05.setAttribute('value', item.regDtm);
            input05.setAttribute('readonly', 'readonly');

            const input06 = document.createElement('input');
            input06.setAttribute('type', 'text');
            input06.setAttribute('class', '');
            input06.setAttribute('value', item.udtId);
            input06.setAttribute('readonly', 'readonly');

            const input07 = document.createElement('input');
            input07.setAttribute('type', 'text');
            input07.setAttribute('class', '');
            input07.setAttribute('value', item.udtDtm);
            input07.setAttribute('readonly', 'readonly');

            const input08 = document.createElement('button');
            input08.setAttribute('type', 'button');
            input08.setAttribute('class', '');
            input08.setAttribute('onClick', 'fn_deleteEntityField(' + item.entityId + ', \'' + item.termPhyNm + '\')');
            input08.innerHTML = '삭제';

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
     * 엔티티배포
     * 엔티티정보를 배포해서 엔티티 파일을 만들고 Git에 push한다.
     */
    depBtn02.addEventListener('click', (e) => {
        if (!confirm("선택하신 엔티티를 배포하시겠습니까?"))
            return;

        let chkArr = new Array();
        const chkList = document.getElementsByClassName('chk');
        for (let i = 0; i < chkList.length; i++) {
            if (chkList[i].checked == true) {
                chkArr.push(chkList[i].value);
            }
        }

        const data = {chkArr: chkArr};
        fn_postAjax('/eims/deployEntity', data, fn_deployEntityCallback);
    })

    /*
     * @Desc
     * 엔티티배포콜백
     * 엔티티배포에 성공하면 페이지를 새로고침한다.
     */
    const fn_deployEntityCallback = (res) => {
        // window.location.reload();
    }