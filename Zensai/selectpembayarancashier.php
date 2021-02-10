<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $paymentmethod = $_POST['paymentmethod'];
   if($paymentmethod == 'method1'){
    $qdinein=mysqli_query($mysqli,"SELECT DISTINCT tbl_pembayaran.id_pembayaran AS id_pembayaran, mst_customer.namalengkap AS namapelanggan, mst_customer.username AS username, tbl_pembayaran.total_pembayaran AS total_pembayaran, tbl_pembayaran.tanggal_pembayaran AS tanggal_pembayaran, tbl_paymentmethod.nama_metode AS nama_metode FROM tbl_pembayaran JOIN mst_customer ON tbl_pembayaran.id_cust = mst_customer.id_cust JOIN tbl_cart ON tbl_pembayaran.id_pembayaran = tbl_cart.id_pembayaran JOIN tbl_paymentmethod ON tbl_pembayaran.id_mtdpembayaran = tbl_paymentmethod.id_mtdpembayaran WHERE (tbl_paymentmethod.nama_metode='cash' OR tbl_paymentmethod.nama_metode='debit' OR tbl_paymentmethod.nama_metode='kredit') AND tbl_pembayaran.status ='wait'");
    if (mysqli_num_rows($qdinein)>0)
    {
     $response["data"]=array();
     while($r=mysqli_fetch_array($qdinein))
     {
     $user=array();
     $user["idpembayaran"]=$r["id_pembayaran"];
      $user["namapelanggan"]=$r["namapelanggan"];
      $user["username"]=$r["username"];
      $user["tglpembayaran"]=$r["tanggal_pembayaran"];
      $user["totalpembayaran"]=$r["total_pembayaran"];
      $user["nama_metode"]=$r["nama_metode"];
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

   }else if($paymentmethod == 'method2'){
    $qtakeaway=mysqli_query($mysqli,"SELECT DISTINCT tbl_pembayaran.id_pembayaran AS id_pembayaran, mst_customer.namalengkap AS namapelanggan, mst_customer.username AS username, tbl_pembayaran.total_pembayaran AS total_pembayaran, tbl_pembayaran.tanggal_pembayaran AS tanggal_pembayaran, tbl_paymentmethod.nama_metode AS nama_metode FROM tbl_pembayaran JOIN mst_customer ON tbl_pembayaran.id_cust = mst_customer.id_cust JOIN tbl_cart ON tbl_pembayaran.id_pembayaran = tbl_cart.id_pembayaran JOIN tbl_paymentmethod ON tbl_pembayaran.id_mtdpembayaran = tbl_paymentmethod.id_mtdpembayaran WHERE tbl_paymentmethod.nama_metode='transfer' AND tbl_pembayaran.status ='wait'");

    if (mysqli_num_rows($qtakeaway)>0)
    {
     $response["data"]=array();
     while($r=mysqli_fetch_array($qtakeaway))
     {
     $user=array();
     $user["idpembayaran"]=$r["id_pembayaran"];
      $user["namapelanggan"]=$r["namapelanggan"];
      $user["username"]=$r["username"];
      $user["tglpembayaran"]=$r["tanggal_pembayaran"];
      $user["totalpembayaran"]=$r["total_pembayaran"];
      $user["nama_metode"]=$r["nama_metode"];
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
   }


   
    // if($qcart){  
    //   $qupdate=mysqli_query($mysqli,"UPDATE tbl_cart SET status = 'approved' WHERE id_cart = '$id_cart' AND status = 'wait'");
    //   $response = array();
    //   if($qupdate){
    //     $response["success"] =1;
    //     $response["message"] = "Data berhasil ditambah";
    //     echo json_encode($response);
    //   }
    //   else{
    //     $response["success"] =0;
    //     $response["message"] = "Data gagal ditambah";
    //     echo json_encode($response);
    //   }
      
    // } 


	  
?>	