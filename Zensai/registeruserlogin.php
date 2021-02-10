<?php
header('Content-type:application/json;charset=utf-8');
include "conn.php";

/*$_POST['username'] = "ckh";
$_POST['password'] = "ckh";
$_POST['email'] = "ckh@mfy.com";
$_POST['status'] = "customer";*/
	
	$username = $_POST['username'];
    $password = $_POST['password'];
    $email = $_POST['email'];
    $status = $_POST['status'];
	
	$q=mysqli_query($mysqli,"INSERT INTO mst_userlogin(username,password,email,level_otoritas) VALUES ('$username','$password','$email','$status')");
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