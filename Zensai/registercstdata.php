<?php
header('Content-type:application/json;charset=utf-8');
include "conn.php";

/*$_POST['username'] = "ckh";
$_POST['firstname'] = "Christofer";
$_POST['lastname'] = "Horas";
$_POST['alamat'] = "Ruko Pascal Timur no.20";
$_POST['notelp'] = "081599218888";*/

$z=mysqli_query($mysqli,"SELECT id_cust FROM mst_customer ORDER BY id_cust DESC LIMIT 1;");

if (mysqli_num_rows($z)>0)
{
   while($r=mysqli_fetch_array($z))
   {
   $lastid =$r["id_cust"];
   }
}

$digita = substr($lastid,9,1);

if($digita < 9){
	$digita = $digita + 1;
	$defaultcode = substr($lastid,0,9);
	$newid = $defaultcode . $digita;
	}
	else{
	   $digitb = substr($lastid,8,2);
	   if($digitb < 99){
		  $digitb = $digitb + 1;
		  $defaultcode = substr($lastid,0,8);
		  $newid = $defaultcode . $digitb;
	   }
	   else{
	   $digitc = substr($lastid,7,3);
	   $digitc = $digitc + 1;
	   $defaultcode = substr($lastid,0,7);
	   $newid = $defaultcode . $digitc;
	   }
	}



	
    $username = $_POST['username'];
    $nama = $_POST['namalengkap'];
    $tgl_lahir = $_POST['tgl_lahir'];
    $alamat = $_POST['alamat'];
	$no_hp = $_POST['no_hp'];
	$kredit = 0;
	
	$q=mysqli_query($mysqli,"INSERT INTO mst_customer(id_cust,username,namalengkap,tgl_lahir,alamat,no_hp,kredit_iklan) VALUES ('$newid','$username','$nama','$tgl_lahir','$alamat','$no_hp','$kredit')");
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