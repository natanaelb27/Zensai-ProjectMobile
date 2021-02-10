<?php
header('Content-type:application/json;charset=utf-8');
include "conn.php";

	$username = $_POST['username'];
    $email = $_POST['email'];

	
	$q=mysqli_query($mysqli,"UPDATE mst_userlogin SET email='$email' WHERE username='$username'");
	$response = array();
	
	if($q){
		$response["success"] =1;
		$response["message"] = "Data berhasil ditambah";
		echo json_encode($response);
	}
	else{
		$response["success"] =0;
		$response["message"] = "Data gagal ditambah";
		echo json_encode($response);
	}
?>