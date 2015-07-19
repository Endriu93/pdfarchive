<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Accept-Language" content="pl">
 <meta charset="UTF-8">
<title>File Uploading Form</title>
</head>
<body>
<h3>File Upload:</h3>
Select a file to upload: ąą<br />
<form action="UploadServlet" method="post"  accept-charset="UTF-8"
                        enctype="multipart/form-data">
<input type="file" name="file" size="50" />
<br />
<input type="submit" value="Upload File" />
</form>
</body>
</html>