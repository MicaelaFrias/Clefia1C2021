window.addEventListener('load', (event) => {
    var spinner = document.getElementById("backdrop");
    spinner.style.display = "block";
    let showSpinner = document.getElementById("isLoading").value;
    if (showSpinner === 'true') {
        setTimeout(function(){
            console.log("Se cargan paths")
            var baseUrl = 'http://localhost:8080/';
            var _imgOriginal = document.getElementById('originalImage');
            var _imgEncrypted = document.getElementById('encryptedImage');
            var _imgDecrypted = document.getElementById('decryptedImage');

            var _originalPath = document.getElementById('originalPath').value;
            var _encryptedPath = document.getElementById('encryptedPath').value;
            var _decryptedPath = document.getElementById('decryptedPath').value;
            console.log(_originalPath)
            console.log(_encryptedPath)
            console.log(_decryptedPath)

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
            console.log("Se asignan paths")
            newImgO.src = baseUrl + _originalPath;
            newImgE.src = baseUrl + _encryptedPath;
            newImgD.src = baseUrl + _decryptedPath;
            console.log(newImgO.src)
            console.log(newImgE.src)
            console.log(newImgD.src)
            spinner.style.display = "none";
        }, 8000);
    }
    spinner.style.display = "none";
});

function showSpinner() {
    var spinner = document.getElementById("backdrop");
    spinner.style.display = "block";
}