

    /*
     * @Desc
     * 엑셀업로드
     */ 
    fn_uploadForm = () => {
        let formData = new FormData(document.getElementById('uploadForm'));

        $.ajax({
            url: '/meta/insertExcelUpload',
            type: 'POST',
            data: formData,
            success: function(e) {
                alert(e);
            },
            error: function(e) {
                alert("ERROR: ", e);
            }
        });
    }