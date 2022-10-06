$(document).ready(function(){
    $("#buttonCancel").on("click", function(){
        window.location = moduleURL;
    });

    $("#fileImage").change(function() {
        fileSize = this.files[0].size;
        // alert("File size: " + fileSize);
        if(fileSize > 5242880){
            this.setCustomValidity("The photo size must be < 10MB");
            this.reportValidity();
        }
        else{
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });
});

