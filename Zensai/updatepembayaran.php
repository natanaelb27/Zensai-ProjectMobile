<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $id_pembayaran = $_POST['id_pembayaran'];
   $paymentmethod = $_POST['paymentmethod'];
   $status = $_POST['status'];
   if($paymentmethod == 'method1'){ 
    $detailmethod = $_POST['detailmethod'];
    if($detailmethod == 'debit'){
      $nomor = $_POST['nomor'];
      $bank = $_POST['bank'];
      $qupdate=mysqli_query($mysqli,"UPDATE tbl_pembayaran SET status = '$status', nomor_kartu = '$nomor', nama_bank = '$bank' WHERE id_pembayaran = '$id_pembayaran' AND status = 'wait'");
      $qupdate2=mysqli_query($mysqli,"UPDATE tbl_cart SET status = 'paid' WHERE id_pembayaran = '$id_pembayaran'");
      $response = array();
      if($qupdate){
        $response["success"] =1;
        $response["message"] = "Data berhasil ditambah";
        echo json_encode($response);
      }
      else{
        $response["success"] =0;
        $response["message"] = "Data gagal ditambah";
        echo json_encode($response);
      }
    }else if($detailmethod == 'kredit'){
      $nomor = $_POST['nomor'];
      $qupdate=mysqli_query($mysqli,"UPDATE tbl_pembayaran SET status = '$status', nomor_kartu = '$nomor' WHERE id_pembayaran = '$id_pembayaran' AND status = 'wait'");
      $qupdate2=mysqli_query($mysqli,"UPDATE tbl_cart SET status = 'paid' WHERE id_pembayaran = '$id_pembayaran'");
      $response = array();
      if($qupdate){
        $response["success"] =1;
        $response["message"] = "Data berhasil ditambah";
        echo json_encode($response);
      }
      else{
        $response["success"] =0;
        $response["message"] = "Data gagal ditambah";
        echo json_encode($response);
      }
    }else if($detailmethod == 'cash'){
      $qupdate=mysqli_query($mysqli,"UPDATE tbl_pembayaran SET status = '$status' WHERE id_pembayaran = '$id_pembayaran' AND status = 'wait'");
      $qupdate2=mysqli_query($mysqli,"UPDATE tbl_cart SET status = 'paid' WHERE id_pembayaran = '$id_pembayaran'");
      $response = array();
      if($qupdate){
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
    
      $checktotalharga=mysqli_query($mysqli,"SELECT * FROM tbl_pembayaran WHERE id_pembayaran = '$id_pembayaran' AND total_pembayaran >= 1000000 AND status = 'approved'");
         if(mysqli_num_rows($checktotalharga)>0){
          while($r = mysqli_fetch_array($checktotalharga)){  
            $id_cust = $r['id_cust'];
          }
          $insertiklan=mysqli_query($mysqli,"INSERT INTO mst_iklan(id_cust, id_pembayaran, status) VALUES('$id_cust', '$id_pembayaran', 'wait')");
        }

      

   }else if($paymentmethod == 'method2'){
    if($status == 'approved'){
      $qupdate=mysqli_query($mysqli,"UPDATE tbl_pembayaran SET status = '$status' WHERE id_pembayaran = '$id_pembayaran' AND status = 'wait'");
      $qupdate2=mysqli_query($mysqli,"UPDATE tbl_cart SET status = 'paid' WHERE id_pembayaran = '$id_pembayaran'");
      $response = array();
      if($qupdate){
        $response["success"] =1;
        $response["message"] = "Data berhasil ditambah";
        echo json_encode($response);
      }
      else{
        $response["success"] =0;
        $response["message"] = "Data gagal ditambah";
        echo json_encode($response);
      }
      $checktotalharga=mysqli_query($mysqli,"SELECT * FROM tbl_pembayaran WHERE id_pembayaran = '$id_pembayaran' AND total_pembayaran >= 1000000 AND status = 'approved'");
      if(mysqli_num_rows($checktotalharga)>0){
        while($r = mysqli_fetch_array($checktotalharga)){  
          $id_cust = $r['id_cust'];
        }
        $insertiklan=mysqli_query($mysqli,"INSERT INTO mst_iklan(id_cust, id_pembayaran, status) VALUES('$id_cust', '$id_pembayaran', 'wait')");
      }

      

    }else if($status == 'refused'){
      $qupdate=mysqli_query($mysqli,"UPDATE tbl_pembayaran SET status = '$status' WHERE id_pembayaran = '$id_pembayaran' AND status = 'wait'");
      $qupdate2=mysqli_query($mysqli,"UPDATE tbl_cart SET status = 'approved' WHERE id_pembayaran = '$id_pembayaran'");
      $response = array();
      if($qupdate){
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

    }
    
   
      
   


	  
?>	