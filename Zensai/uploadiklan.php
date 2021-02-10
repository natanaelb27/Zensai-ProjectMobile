<?php
header('content-type:application/json;charset=utf-8');
include "conn.php";
 

	$image = $_POST['imageurl'];
	$username = $_POST['username'];
	$id_iklan = $_POST['id_iklan'];
	$keterangan = $_POST['text_iklan'];
	if($keterangan==""){
		if($image=="kosong"){ //Impossible
			$sqli=mysqli_query($mysqli, "UPDATE mst_iklan SET keterangan = '$keterangan', status='wait' WHERE id_iklan='$id_iklan'");
		}else{
			$id = uniqid($username, true);
	 		$p = "iklanpic/$id.jpeg";
	 		$path = "https://10.0.2.2/Zensai/$p";
	 		$sqli=mysqli_query($mysqli, "UPDATE mst_iklan SET keterangan = null, linkpiciklan = '".$path."', status='waitadmin' WHERE id_iklan='$id_iklan'");
	 		if($sqli){
	 			file_put_contents($p,base64_decode($image));
 			}
 		}
	}else{
		if($image=="kosong"){
			$sqli=mysqli_query($mysqli, "UPDATE mst_iklan SET keterangan = '$keterangan', status='waitadmin' WHERE id_iklan='$id_iklan'");
		}else{
			$id = uniqid($username, true);
	 		$p = "iklanpic/$id.jpeg";
	 		$path = "https://10.0.2.2/Zensai/$p";
	 		$sqli=mysqli_query($mysqli, "UPDATE mst_iklan SET keterangan = '$keterangan', linkpiciklan = '".$path."', status='waitadmin' WHERE id_iklan='$id_iklan'");
	 		if($sqli){
	 			file_put_contents($p,base64_decode($image));
 			}
 		}
	}
?>