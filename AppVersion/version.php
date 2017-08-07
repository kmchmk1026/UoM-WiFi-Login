<?php
    $jsonObj = new stdClass();
    $jsonObj->newversion = 1;
    $jsonObj->title = "New Update";
    $jsonObj->message = "A new update (1.76MB) is available for this application.\n * Performance improved.\n* Bugs fixed.\n* (Background login is not supported for Huawei yet)";//\n(Use another browser other than Chrome)";
    $jsonObj->positivebutton = "Download";
    $jsonObj->negativebutton = "Cancel";
    $jsonObj->apkurl = "https://www.dropbox.com/sh/bmmfxrx2yfpjf59/AACIXoJ7_RnSnmTeoZEpkEeDa?dl=0";
    // $jsonObj->apkurl = "http://13.58.202.127/UoM_Wireless_App/UoM_Wireless.apk";
    echo json_encode($jsonObj);




if (isset($_GET["device"])) {
    $device = $_GET["device"];

	$servername = "localhost";
	$username = "root";
	$password = "1234";
	$dbname = "uom_wireless";
	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);
	$conn->set_charset("utf8");
	// Check connection
	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	}



    $sql = "insert into log (device,time) values (?, NOW())";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param('s', $device);
    $stmt->execute();

}
?>


