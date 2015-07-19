<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>File Uploading Form</title>
</head>
<body>
<h3>File Upload:</h3>
Select a file to upload: ąą <br />
<form action="UploadServlet" method="post"  
                        enctype="multipart/form-data">
<input type="file" name="file" size="50" />
<br />
<input type="submit" value="Upload File" />
</form>
</body>
</html>