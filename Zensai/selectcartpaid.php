<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $q=mysqli_query($mysqli,"SELECT tbl_cart.id_cart AS id_cart, mst_customer.namalengkap AS namapelanggan, mst_customer.username AS username, tbl_pembayaran.tanggal_pembayaran AS tanggal_pembayaran, tbl_cart.service AS service, mst_menu.nama_menu AS nama_menu, tbl_cart.quantity AS quantity, mst_menu.harga AS harga, mst_menu.harga * tbl_cart.quantity AS subtotal FROM tbl_pembayaran JOIN mst_customer ON tbl_pembayaran.id_cust = mst_customer.id_cust JOIN tbl_cart ON tbl_pembayaran.id_pembayaran = tbl_cart.id_pembayaran JOIN tbl_paymentmethod ON tbl_pembayaran.id_mtdpembayaran = tbl_paymentmethod.id_mtdpembayaran JOIN mst_menu ON tbl_cart.id_menu = mst_menu.id_menu WHERE tbl_cart.status ='paid'");
   $response=array();
   if (mysqli_num_rows($q)>0)
    {
     $response["data"]=array();
     while($r=mysqli_fetch_array($q))
     {
     $user=array();
     $user["id_cart"]=$r["id_cart"];
      $user["namapelanggan"]=$r["namapelanggan"];
      $user["username"]=$r["username"];
      $user["tglpembayaran"]=$r["tanggal_pembayaran"];
      $user["service"]=$r["service"];
      $user["nama_menu"]=$r["nama_menu"];
      $user["quantity"]=$r["quantity"];
      $user["harga"]=$r["harga"];
      $user["subtotal"]=$r["subtotal"];
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