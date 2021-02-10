<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $username = $_POST['username'];

   $q=mysqli_query($mysqli,"SELECT mst_menu.nama_menu AS nama_menu, mst_menu.deskripsi_menu AS deskripsi_menu, mst_menu.harga AS harga, mst_menu.linkgambarmenu AS linkgambarmenu, tbl_cart.quantity AS quantity FROM tbl_cart JOIN mst_customer ON tbl_cart.id_cust = mst_customer.id_cust JOIN mst_menu ON tbl_cart.id_menu = mst_menu.id_menu WHERE mst_customer.username = '$username' AND tbl_cart.status = 'pending'");
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
      $user["quantity"]=$r["quantity"];
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