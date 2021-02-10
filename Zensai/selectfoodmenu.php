<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $q=mysqli_query($mysqli,"select * from mst_menu where id_menu like 'FOD%'");
   $response=array();
   
   if (mysqli_num_rows($q)>0)
   {
	  $response["data"]=array();
	  while($r=mysqli_fetch_array($q))
	  {
	  $user=array();
	  $user["nama_menu"]=$r["nama_menu"];
      $user["deskripsi_menu"]=$r["deskripsi_menu"];
      $user["harga"]=$r["harga"];
      $user["linkgambarmenu"]=$r["linkgambarmenu"];
	  array_push($response["data"],$user);
	  }
      $response["success"]=1;
	  $response["message"]="data berhasil diambil";
      echo json_encode($response);
   }
   else
   { 
	  $response["success"]=-1;
	  $response["message"]="data kosong";
	  echo json_encode($response);

	}
	  
?>	