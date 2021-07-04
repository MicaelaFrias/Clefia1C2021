window.addEventListener('load', (event) => {
    var spinner = document.getElementById("backdrop");
    spinner.style.display = "block";
    let showSpinner = document.getElementById("isLoading").value;
    if (showSpinner === 'true') {
        setTimeout(function(){
            var baseUrl = 'data:image/png;base64,';
            var _imgOriginal = document.getElementById('originalImage');
            var _imgEncrypted = document.getElementById('encryptedImage');
            var _imgDecrypted = document.getElementById('decryptedImage');

            var _originalPath = document.getElementById('originalPath').value;
            var _encryptedPath = document.getElementById('encryptedPath').value;
            var _decryptedPath = document.getElementById('decryptedPath').value;

            var newImgO = new Image;
            var newImgE = new Image;
            var newImgD = new Image;
            newImgO.onload = function() {
                _imgOriginal.src = this.src;
            }
            newImgE.onload = function() {
                _imgEncrypted.src = this.src;
            }
            newImgD.onload = function() {
                _imgDecrypted.src = this.src;
            }
            newImgO.src = 'http://localhost:8080/' + _originalPath;
            newImgE.src = baseUrl + _encryptedPath;
            newImgD.src = baseUrl + _decryptedPath;
            spinner.style.display = "none";
        }, 8000);
    } else {
        spinner.style.display = "none";
    }
});

function showSpinner() {
    var spinner = document.getElementById("backdrop");
    spinner.style.display = "block";
}