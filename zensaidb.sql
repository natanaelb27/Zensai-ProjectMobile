-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 25, 2020 at 04:31 PM
-- Server version: 10.4.6-MariaDB
-- PHP Version: 7.3.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `zensaidb`
--

-- --------------------------------------------------------

--
-- Table structure for table `mst_customer`
--

CREATE TABLE `mst_customer` (
  `id_cust` char(10) NOT NULL,
  `username` varchar(20) DEFAULT NULL,
  `namalengkap` varchar(30) DEFAULT NULL,
  `tgl_lahir` date DEFAULT NULL,
  `alamat` varchar(50) DEFAULT NULL,
  `no_hp` varchar(20) DEFAULT NULL,
  `kredit_iklan` int(11) DEFAULT NULL,
  `linkprofilepic` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mst_customer`
--

INSERT INTO `mst_customer` (`id_cust`, `username`, `namalengkap`, `tgl_lahir`, `alamat`, `no_hp`, `kredit_iklan`, `linkprofilepic`) VALUES
('CST0000001', 'admincs', 'Admin Customer', '2000-01-19', 'Ruko Pascal Timur', '085229127731', 0, 'https://10.0.2.2/Zensai/profilepic/admincs5f008cdb9d74b6.52716275.jpeg'),
('CST0000002', 'ckemal', 'Christofer Kemal', '2007-11-02', 'Jl. Daeng Tompo no.23', '081234512312', 0, 'https://10.0.2.2/Zensai/profilepic/ckemal5f2bc1cf11e0b0.86454650.jpeg'),
('CST0000003', 'riri', 'Riri Kiki', '2001-11-12', 'Jl. Merak No. 9', '08123451234578', 0, 'https://10.0.2.2/Zensai/profilepic/riri5fbde283228943.37156792.jpeg');

-- --------------------------------------------------------

--
-- Table structure for table `mst_iklan`
--

CREATE TABLE `mst_iklan` (
  `id_iklan` int(11) NOT NULL,
  `id_cust` char(10) DEFAULT NULL,
  `id_pembayaran` int(11) NOT NULL,
  `linkpiciklan` varchar(100) DEFAULT NULL,
  `keterangan` varchar(50) DEFAULT NULL,
  `tgl_tayang` date DEFAULT NULL,
  `tgl_turun` date DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mst_iklan`
--

INSERT INTO `mst_iklan` (`id_iklan`, `id_cust`, `id_pembayaran`, `linkpiciklan`, `keterangan`, `tgl_tayang`, `tgl_turun`, `status`) VALUES
(5, 'CST0000002', 11, 'https://10.0.2.2/Zensai/iklanpic/ckemal5fbe597683a3b4.61808408.jpeg', 'Beli 5 Gratis 1', '2020-11-25', '2020-12-02', 'approved');

-- --------------------------------------------------------

--
-- Table structure for table `mst_menu`
--

CREATE TABLE `mst_menu` (
  `id_menu` char(10) NOT NULL,
  `nama_menu` varchar(30) DEFAULT NULL,
  `deskripsi_menu` varchar(100) DEFAULT NULL,
  `harga` int(11) DEFAULT NULL,
  `linkgambarmenu` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mst_menu`
--

INSERT INTO `mst_menu` (`id_menu`, `nama_menu`, `deskripsi_menu`, `harga`, `linkgambarmenu`) VALUES
('FOD0000001', 'Nigiri Set A', 'Set sushi nigiri berisi salmon, maguro, tamagoyaki, dan unagi masing masing 2 pcs', 75000, 'http://10.0.2.2/Zensai/menupic/nigiriseta.png'),
('FOD0000002', 'Nigiri Set B', 'Set sushi nigiri berisi salmon, tako, kani, dan tamagoyaki masing masing 2 pcs', 65000, 'http://10.0.2.2/Zensai/menupic/nigirisetb.png');

-- --------------------------------------------------------

--
-- Table structure for table `mst_userlogin`
--

CREATE TABLE `mst_userlogin` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `email` varchar(30) DEFAULT NULL,
  `level_otoritas` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mst_userlogin`
--

INSERT INTO `mst_userlogin` (`username`, `password`, `email`, `level_otoritas`) VALUES
('admin', 'admin', 'adminzenzai@gmail.com', 'admin'),
('admincs', 'admincs', 'admincs@mafuyu.com', 'customer'),
('cashier1', 'cashier1', 'cashierzenzai@gmail.com', 'cashier'),
('ckemal', 'ckemal', 'christo@mafuyu.com', 'customer'),
('kitchen1', 'kitchen1', 'kitchenzensai@gmail.com', 'kitchen'),
('riri', 'riri', 'riri@gmail.com', 'customer');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_cart`
--

CREATE TABLE `tbl_cart` (
  `id_cart` int(11) NOT NULL,
  `id_cust` char(10) CHARACTER SET utf8mb4 NOT NULL,
  `id_menu` char(10) CHARACTER SET utf8mb4 NOT NULL,
  `id_pembayaran` int(11) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `quantity` int(11) NOT NULL,
  `status` varchar(10) CHARACTER SET utf8mb4 NOT NULL,
  `service` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_cart`
--

INSERT INTO `tbl_cart` (`id_cart`, `id_cust`, `id_menu`, `id_pembayaran`, `timestamp`, `quantity`, `status`, `service`) VALUES
(20, 'CST0000002', 'FOD0000001', 11, '2020-11-25 13:17:03', 10, 'done', 'dine in'),
(21, 'CST0000002', 'FOD0000002', 11, '2020-11-25 13:17:06', 5, 'done', 'dine in'),
(22, 'CST0000002', 'FOD0000002', 12, '2020-11-25 15:25:26', 1, 'done', 'dine in');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_paymentmethod`
--

CREATE TABLE `tbl_paymentmethod` (
  `id_mtdpembayaran` char(10) NOT NULL,
  `nama_metode` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_paymentmethod`
--

INSERT INTO `tbl_paymentmethod` (`id_mtdpembayaran`, `nama_metode`) VALUES
('MTD0000001', 'cash'),
('MTD0000002', 'debit'),
('MTD0000003', 'kredit'),
('MTD0000004', 'transfer');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_pembayaran`
--

CREATE TABLE `tbl_pembayaran` (
  `id_pembayaran` int(11) NOT NULL,
  `id_cust` char(10) CHARACTER SET utf8mb4 NOT NULL,
  `id_mtdpembayaran` char(10) CHARACTER SET utf8mb4 DEFAULT NULL,
  `total_pembayaran` int(11) NOT NULL,
  `tanggal_pembayaran` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `bukti_pembayaran` char(100) NOT NULL,
  `nomor_kartu` varchar(16) NOT NULL,
  `nama_bank` varchar(30) NOT NULL,
  `status` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_pembayaran`
--

INSERT INTO `tbl_pembayaran` (`id_pembayaran`, `id_cust`, `id_mtdpembayaran`, `total_pembayaran`, `tanggal_pembayaran`, `bukti_pembayaran`, `nomor_kartu`, `nama_bank`, `status`) VALUES
(11, 'CST0000002', 'MTD0000003', 1075000, '2020-11-25 13:16:59', '', '1234567890123456', '', 'approved'),
(12, 'CST0000002', 'MTD0000004', 65000, '2020-11-25 15:25:21', 'https://10.0.2.2/Zensai/buktipembayaran/ckemal5fbe69cc33d7a6.94516492.jpeg', '', '', 'approved');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `mst_customer`
--
ALTER TABLE `mst_customer`
  ADD PRIMARY KEY (`id_cust`),
  ADD KEY `username` (`username`);

--
-- Indexes for table `mst_iklan`
--
ALTER TABLE `mst_iklan`
  ADD PRIMARY KEY (`id_iklan`),
  ADD KEY `id_cust` (`id_cust`),
  ADD KEY `id_pembayaran` (`id_pembayaran`);

--
-- Indexes for table `mst_menu`
--
ALTER TABLE `mst_menu`
  ADD PRIMARY KEY (`id_menu`);

--
-- Indexes for table `mst_userlogin`
--
ALTER TABLE `mst_userlogin`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `tbl_cart`
--
ALTER TABLE `tbl_cart`
  ADD PRIMARY KEY (`id_cart`),
  ADD KEY `id_cust` (`id_cust`),
  ADD KEY `id_menu` (`id_menu`),
  ADD KEY `id_pembayaran` (`id_pembayaran`);

--
-- Indexes for table `tbl_paymentmethod`
--
ALTER TABLE `tbl_paymentmethod`
  ADD PRIMARY KEY (`id_mtdpembayaran`);

--
-- Indexes for table `tbl_pembayaran`
--
ALTER TABLE `tbl_pembayaran`
  ADD PRIMARY KEY (`id_pembayaran`),
  ADD KEY `id_cust` (`id_cust`),
  ADD KEY `id_mtdpembayaran` (`id_mtdpembayaran`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `mst_iklan`
--
ALTER TABLE `mst_iklan`
  MODIFY `id_iklan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tbl_cart`
--
ALTER TABLE `tbl_cart`
  MODIFY `id_cart` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `tbl_pembayaran`
--
ALTER TABLE `tbl_pembayaran`
  MODIFY `id_pembayaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `mst_customer`
--
ALTER TABLE `mst_customer`
  ADD CONSTRAINT `mst_customer_ibfk_1` FOREIGN KEY (`username`) REFERENCES `mst_userlogin` (`username`);

--
-- Constraints for table `mst_iklan`
--
ALTER TABLE `mst_iklan`
  ADD CONSTRAINT `mst_iklan_ibfk_1` FOREIGN KEY (`id_cust`) REFERENCES `mst_customer` (`id_cust`),
  ADD CONSTRAINT `mst_iklan_ibfk_2` FOREIGN KEY (`id_pembayaran`) REFERENCES `tbl_pembayaran` (`id_pembayaran`);

--
-- Constraints for table `tbl_cart`
--
ALTER TABLE `tbl_cart`
  ADD CONSTRAINT `tbl_cart_ibfk_1` FOREIGN KEY (`id_cust`) REFERENCES `mst_customer` (`id_cust`),
  ADD CONSTRAINT `tbl_cart_ibfk_2` FOREIGN KEY (`id_menu`) REFERENCES `mst_menu` (`id_menu`),
  ADD CONSTRAINT `tbl_cart_ibfk_3` FOREIGN KEY (`id_pembayaran`) REFERENCES `tbl_pembayaran` (`id_pembayaran`);

--
-- Constraints for table `tbl_pembayaran`
--
ALTER TABLE `tbl_pembayaran`
  ADD CONSTRAINT `tbl_pembayaran_ibfk_1` FOREIGN KEY (`id_cust`) REFERENCES `mst_customer` (`id_cust`),
  ADD CONSTRAINT `tbl_pembayaran_ibfk_2` FOREIGN KEY (`id_mtdpembayaran`) REFERENCES `tbl_paymentmethod` (`id_mtdpembayaran`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
