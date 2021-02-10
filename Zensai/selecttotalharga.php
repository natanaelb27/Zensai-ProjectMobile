<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $username = $_POST['username'];
   $from = $_POST['from'];

   if($from == 'prosespembayaran'){
       $q=mysqli_query($mysqli,"SELECT mst_menu.harga AS harga, tbl_cart.quantity AS quantity FROM tbl_cart JOIN mst_customer ON tbl_cart.id_cust = mst_customer.id_cust JOIN mst_menu ON tbl_cart.id_menu = mst_menu.id_menu WHERE mst_customer.username = '$username' AND tbl_cart.status = 'approved'");
   }else if($from == 'cartcustomer'){
      $q=mysqli_query($mysqli,"SELECT mst_menu.harga AS harga, tbl_cart.quantity AS quantity FROM tbl_cart JOIN mst_customer ON tbl_cart.id_cust = mst_customer.id_cust JOIN mst_menu ON tbl_cart.id_menu = mst_menu.id_menu WHERE mst_customer.username = '$username' AND tbl_cart.status = 'pending'");
   }
 
   $response=array();
   if (mysqli_num_rows($q)>0)
   {
	  $response["data"]=array();
	  while($r=mysqli_fetch_array($q))
	  {
      $user=array();

     $user["totalharga"]=$r["harga"] * $r["quantity"];
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