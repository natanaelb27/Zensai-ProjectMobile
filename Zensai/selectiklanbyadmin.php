<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $q=mysqli_query($mysqli,"SELECT mst_iklan.id_iklan AS id_iklan, mst_customer.namalengkap AS namapelanggan, mst_customer.username AS username, tbl_pembayaran.tanggal_pembayaran AS tglpembayaran FROM mst_iklan JOIN tbl_pembayaran ON mst_iklan.id_pembayaran = tbl_pembayaran.id_pembayaran JOIN mst_customer ON mst_iklan.id_cust = mst_customer.id_cust WHERE mst_iklan.status='waitadmin'");
   $response=array();

   if(mysqli_num_rows($q)>0){
    $response["data"] = array();
    while($r = mysqli_fetch_array($q)){   
      $user = array();
      $user["id_iklan"]=$r["id_iklan"];
      $user["namapelanggan"]=$r["namapelanggan"];
      $user["username"]=$r["username"];
      $user["tglpembayaran"]=$r["tglpembayaran"];
      array_push($response["data"], $user);
    }
    $response["success"] = 1;
    $response["message"] = "Data berhasil dibaca";
    echo json_encode($response);
  } else {
    $response["success"] = 0;
    $response["message"] = "Tidak ada data";
    echo json_encode($response);
  }
?>