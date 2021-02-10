<?php
header('Content-type:application/json;charset=utf-8');
include "conn.php";
	
	$status = $_POST['status'];
	$username = $_POST['username'];
    $nama = $_POST['namalengkap'];
    $tgl_lahir = $_POST['tgl_lahir'];
    $alamat = $_POST['alamat'];
	$no_hp = $_POST['no_hp'];
	if($status=='imgchanged'){
		$image = $_POST['imageurl'];
		$id = uniqid($username, true);
		$p = "profilepic/$id.jpeg";
		$path = "https://10.0.2.2/Zensai/$p";
		$q=mysqli_query($mysqli,"UPDATE mst_customer SET namalengkap='$nama', tgl_lahir='$tgl_lahir', alamat='$alamat', no_hp='$no_hp', linkprofilepic ='".$path."' WHERE username='$username'");
		$response = array();
		
		if($q){
			file_put_contents($p,base64_decode($image));
			$response["success"] =1;
			$response["message"] = "Data berhasil ditambah";
			echo json_encode($response);
		}
		else{
			$response["success"] =0;
			$response["message"] = "Data gagal ditambah";
			echo json_encode($response);
		}
	}else if($status=='imgnotchanged'){
		$q=mysqli_query($mysqli,"UPDATE mst_customer SET namalengkap='$nama', tgl_lahir='$tgl_lahir', alamat='$alamat', no_hp='$no_hp' WHERE username='$username'");
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
	}

	
?>