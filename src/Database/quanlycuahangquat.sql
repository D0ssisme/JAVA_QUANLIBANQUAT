-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 06, 2025 lúc 06:25 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `quanlycuahangquat`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitiet_hoadon`
--

CREATE TABLE `chitiet_hoadon` (
  `MaHoaDon` varchar(50) NOT NULL,
  `MaQuat` varchar(50) NOT NULL,
  `SoLuong` int(11) NOT NULL,
  `DonGia` int(11) NOT NULL,
  `ThanhTien` int(11) NOT NULL,
  `MaBaoHanh` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitiet_phieunhap`
--

CREATE TABLE `chitiet_phieunhap` (
  `MaPhieuNhap` varchar(50) NOT NULL,
  `MaQuat` varchar(50) NOT NULL,
  `SoLuong` int(11) NOT NULL,
  `DonGia` int(11) NOT NULL,
  `ThanhTien` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chitiet_phieunhap`
--

INSERT INTO `chitiet_phieunhap` (`MaPhieuNhap`, `MaQuat`, `SoLuong`, `DonGia`, `ThanhTien`) VALUES
('PN001', 'Q001', 10, 440000, 4400000),
('PN001', 'Q002', 5, 520000, 2600000),
('PN002', 'Q003', 8, 280000, 2240000),
('PN002', 'Q004', 6, 376000, 2256000),
('PN003', 'Q005', 7, 1000000, 7000000),
('PN003', 'Q006', 3, 1760000, 5280000),
('PN004', 'Q007', 4, 200000, 800000),
('PN004', 'Q008', 9, 784000, 7056000),
('PN005', 'Q009', 3, 448000, 1344000),
('PN005', 'Q010', 2, 2880000, 5760000),
('PN006', 'Q011', 10, 600000, 6000000),
('PN006', 'Q012', 5, 328000, 1640000),
('PN007', 'Q013', 4, 496000, 1984000),
('PN007', 'Q014', 6, 1920000, 11520000),
('PN008', 'Q015', 3, 392000, 1176000),
('PN009', 'Q001', 6, 440000, 2640000),
('PN009', 'Q005', 4, 1000000, 4000000),
('PN010', 'Q002', 10, 520000, 5200000),
('PN011', 'Q003', 8, 280000, 2240000),
('PN011', 'Q010', 2, 2880000, 5760000),
('PN012', 'Q006', 5, 1760000, 8800000),
('PN013', 'Q007', 7, 200000, 1400000),
('PN013', 'Q014', 3, 1920000, 5760000),
('PN014', 'Q008', 4, 784000, 3136000),
('PN015', 'Q009', 6, 448000, 2688000),
('PN015', 'Q011', 2, 600000, 1200000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `MaHoaDon` varchar(50) NOT NULL,
  `MaKhachHang` varchar(50) NOT NULL,
  `MaNhanVien` varchar(50) NOT NULL,
  `NgayLap` date NOT NULL,
  `MaSuKienKM` varchar(50) DEFAULT NULL,
  `TongTien` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `MaKhachHang` varchar(50) NOT NULL,
  `HoTenKH` varchar(200) NOT NULL,
  `Sdt_KH` varchar(11) NOT NULL,
  `DiaChiKH` varchar(300) NOT NULL,
  `TongTienDaMua` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
ALTER TABLE khachhang
  ADD COLUMN TrangThai TINYINT(1) NOT NULL DEFAULT 1;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaisanpham`
--

CREATE TABLE `loaisanpham` (
  `MaLoaiSanPham` varchar(10) NOT NULL,
  `TenLoai` varchar(100) DEFAULT NULL,
  `TrangThai` varchar(100) DEFAULT NULL,
  `MoTa` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `loaisanpham`
--

INSERT INTO `loaisanpham` (`MaLoaiSanPham`, `TenLoai`, `TrangThai`, `MoTa`) VALUES
('LSP001', 'Quạt trần', 'Hoạt động', 'Quạt gắn trên trần, tiết kiệm không gian'),
('LSP002', 'Quạt đứng', 'Hoạt động', 'Quạt có chân đế điều chỉnh chiều cao'),
('LSP003', 'Quạt bàn', 'Hoạt động', 'Quạt nhỏ gọn đặt trên bàn làm việc.'),
('LSP004', 'Quạt hộp', 'Hoạt động', 'Quạt hình hộp, thiết kế an toàn cho trẻ em'),
('LSP005', 'Quạt sạc', 'Hoạt động', 'Quạt dùng pin sạc, tiện lợi khi mất điện');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvien`
--

CREATE TABLE `nhanvien` (
  `MaNhanVien` varchar(50) NOT NULL,
  `HoTenNV` varchar(200) NOT NULL,
  `ChucVu` varchar(200) NOT NULL,
  `Sdt_NV` varchar(11) NOT NULL,
  `DiaChiNV` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nhanvien`
--

INSERT INTO `nhanvien` (`MaNhanVien`, `HoTenNV`, `ChucVu`, `Sdt_NV`, `DiaChiNV`) VALUES
('NV001', 'Nguyễn Văn A', 'Quản lý', '0901234567', '12 Cách Mạng Tháng 8, TP.HCM'),
('NV002', 'Trần Thị B', 'Nhân viên', '0912345678', '34 Hai Bà Trưng, Hà Nội'),
('NV003', 'Lê Văn C', 'Nhân viên', '0934567890', '56 Nguyễn Huệ, Đà Nẵng');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nha_cung_cap`
--

CREATE TABLE `nha_cung_cap` (
  `MaNCC` varchar(50) NOT NULL,
  `TenNCC` varchar(200) NOT NULL,
  `DiaChiNCC` varchar(300) NOT NULL,
  `Sdt_NCC` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nha_cung_cap`
--

INSERT INTO `nha_cung_cap` (`MaNCC`, `TenNCC`, `DiaChiNCC`, `Sdt_NCC`) VALUES
('NCC001', 'Toshiba Việt Nam', '123 Lê Lợi, TP.HCM', '0123456789'),
('NCC002', 'Midea Corporation', '456 Nguyễn Trãi, Hà Nội', '0987654321'),
('NCC003', 'Panasonic Việt Nam', '789 Trần Hưng Đạo, Đà Nẵng', '0912345678'),
('NCC004', 'Tefan Việt Nam', 'Đức Hòa, Long An', '0338740832');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nha_san_xuat`
--

CREATE TABLE `nha_san_xuat` (
  `MaNSX` varchar(50) NOT NULL,
  `TenNSX` varchar(200) NOT NULL,
  `DiaChi` varchar(300) NOT NULL,
  `Sdt_NSX` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nha_san_xuat`
--

INSERT INTO `nha_san_xuat` (`MaNSX`, `TenNSX`, `DiaChi`, `Sdt_NSX`) VALUES
('NSX001', 'Panasonic', '123 Nguyễn Văn Linh, Hà Nội', '0901234567'),
('NSX002', 'Midea', '456 Lê Duẩn, TP.HCM', '0912345678'),
('NSX003', 'Asia', '789 Trường Chinh, Đà Nẵng', '0923456789'),
('NSX004', 'Toshiba', '12 Nguyễn Huệ, Hà Nội', '0934567890'),
('NSX005', 'Samsung', '45 Nguyễn Văn Linh, TP.HCM', '0945678901'),
('NSX006', 'LG', '78 Lý Tự Trọng, Đà Nẵng', '0956789012'),
('NSX007', 'Sony', '90 Nguyễn Trãi, TP.HCM', '0967890123'),
('NSX008', 'Hitachi', '21 Trần Hưng Đạo, Hà Nội', '0978901234'),
('NSX009', 'Sharp', '34 Tôn Đức Thắng, TP.HCM', '0989012345'),
('NSX010', 'Daikin', '56 Cách Mạng Tháng 8, Đà Nẵng', '0990123456'),
('NSX011', 'Electrolux', '11 Hai Bà Trưng, Hà Nội', '0901123456'),
('NSX012', 'Aqua', '67 Phạm Văn Đồng, TP.HCM', '0912233445'),
('NSX013', 'Kangaroo', '89 Nguyễn Thái Học, Hà Nội', '0923344556'),
('NSX014', 'Sanaky', '101 Trường Sa, TP.HCM', '0934455667'),
('NSX015', 'BlueStone', '112 Hoàng Văn Thụ, Đà Nẵng', '0945566778');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phieunhap`
--

CREATE TABLE `phieunhap` (
  `MaPhieuNhap` varchar(50) NOT NULL,
  `NgayNhap` date NOT NULL,
  `MaNCC` varchar(50) NOT NULL,
  `MaNhanVien` varchar(50) NOT NULL,
  `TongTien` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `phieunhap`
--

INSERT INTO `phieunhap` (`MaPhieuNhap`, `NgayNhap`, `MaNCC`, `MaNhanVien`, `TongTien`) VALUES
('PN001', '2024-04-01', 'NCC001', 'NV001', 7000000),
('PN002', '2024-04-05', 'NCC002', 'NV002', 4496000),
('PN003', '2024-04-08', 'NCC003', 'NV003', 12280000),
('PN004', '2024-04-04', 'NCC001', 'NV001', 7856000),
('PN005', '2024-04-05', 'NCC002', 'NV002', 7104000),
('PN006', '2024-04-06', 'NCC003', 'NV003', 7640000),
('PN007', '2024-04-07', 'NCC001', 'NV001', 13504000),
('PN008', '2024-04-08', 'NCC002', 'NV002', 1176000),
('PN009', '2024-04-09', 'NCC003', 'NV003', 6640000),
('PN010', '2024-04-10', 'NCC001', 'NV001', 5200000),
('PN011', '2024-04-11', 'NCC002', 'NV002', 8000000),
('PN012', '2024-04-12', 'NCC003', 'NV003', 8800000),
('PN013', '2024-04-13', 'NCC001', 'NV001', 7160000),
('PN014', '2024-04-14', 'NCC002', 'NV002', 3136000),
('PN015', '2024-04-15', 'NCC003', 'NV003', 3888000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `quanlibaohanh`
--

CREATE TABLE `quanlibaohanh` (
  `MaBaoHanh` varchar(50) NOT NULL,
  `MaQuat` varchar(50) NOT NULL,
  `MaKhachHang` varchar(50) NOT NULL,
  `ThoiGianBaoHanh` date NOT NULL,
  `TrangThai` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `quanlibaohanh`
--

INSERT INTO `quanlibaohanh` (`MaBaoHanh`, `MaQuat`, `MaKhachHang`, `ThoiGianBaoHanh`, `TrangThai`) VALUES
('BH003', 'Q003', 'KH003', '2025-05-03', 'Còn hạn'),
('BH004', 'Q004', 'KH005', '2025-05-03', 'Còn hạn'),
('BH005', 'Q001', 'KH005', '2025-05-03', 'Còn hạn'),
('BH006', 'Q005', 'KH007', '2025-05-03', 'Còn hạn'),
('BH007', 'Q006', 'KH007', '2025-05-03', 'Còn hạn'),
('BH008', 'Q004', 'KH009', '2025-05-03', 'Còn hạn'),
('BH009', 'Q007', 'KH009', '2025-05-03', 'Còn hạn'),
('BH010', 'Q003', 'KH011', '2025-05-03', 'Còn hạn'),
('BH011', 'Q005', 'KH011', '2025-05-03', 'Còn hạn'),
('BH012', 'Q008', 'KH013', '2025-05-03', 'Còn hạn'),
('BH013', 'Q009', 'KH013', '2025-05-03', 'Còn hạn'),
('BH014', 'Q006', 'KH004', '2025-05-03', 'Còn hạn'),
('BH015', 'Q007', 'KH006', '2025-05-03', 'Còn hạn'),
('BH016', 'Q010', 'KH006', '2025-05-03', 'Còn hạn'),
('BH017', 'Q001', 'KH004', '2025-05-03', 'Còn hạn'),
('BH018', 'Q002', 'KH004', '2025-05-03', 'Còn hạn'),
('BH019', 'Q008', 'KH010', '2025-05-03', 'Còn hạn'),
('BH020', 'Q011', 'KH010', '2025-05-03', 'Còn hạn'),
('BH021', 'Q003', 'KH012', '2025-05-03', 'Còn hạn'),
('BH022', 'Q005', 'KH014', '2025-05-03', 'Còn hạn'),
('BH023', 'Q010', 'KH014', '2025-05-03', 'Còn hạn'),
('BH024', 'Q012', 'KH015', '2025-05-03', 'Còn hạn'),
('BH025', 'Q013', 'KH015', '2025-05-03', 'Còn hạn');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `quat`
--

CREATE TABLE `quat` (
  `MaQuat` varchar(50) NOT NULL,
  `TenQuat` varchar(200) NOT NULL,
  `Gia` int(11) NOT NULL,
  `SoLuongTon` int(50) NOT NULL,
  `MaNSX` varchar(50) NOT NULL,
  `NgaySanXuat` date NOT NULL,
  `ChatLieu` varchar(100) NOT NULL,
  `ThuongHieu` varchar(100) NOT NULL,
  `MaLoaiSP` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `quat`
--

INSERT INTO `quat` (`MaQuat`, `TenQuat`, `Gia`, `SoLuongTon`, `MaNSX`, `NgaySanXuat`, `ChatLieu`, `ThuongHieu`, `MaLoaiSP`) VALUES
('Q001', 'Quạt đứng Toshiba Remote', 550000, 24, 'NSX003', '2024-03-01', 'Nhựa cao cấp', 'Toshiba', 'LSP01'),
('Q002', 'Quạt treo Midea 3 cánh', 650000, 36, 'NSX002', '2024-03-15', 'Kim loại + nhựa', 'Midea', 'LSP02'),
('Q003', 'Quạt mini Panasonic sạc USB', 350000, 57, 'NSX001', '2024-02-20', 'Nhựa ABS', 'Panasonic', 'LSP03'),
('Q004', 'Quạt Hộp Toshiba', 470000, 54, 'NSX004', '2023-04-12', 'Nhựa cứng', 'Toshiba', 'LSP02'),
('Q005', 'Quạt Hơi Nước Samsung', 1250000, 27, 'NSX005', '2023-02-05', 'Nhựa', 'Samsung', 'LSP03'),
('Q006', 'Quạt Điều Hòa LG', 2200000, 36, 'NSX006', '2023-01-25', 'Hợp kim', 'LG', 'LSP03'),
('Q007', 'Quạt Mini Sony', 250000, 32, 'NSX007', '2023-07-18', 'Nhựa mềm', 'Sony', 'LSP04'),
('Q008', 'Quạt Đứng Hitachi', 980000, 38, 'NSX008', '2023-08-08', 'Nhựa', 'Hitachi', 'LSP01'),
('Q009', 'Quạt Treo Sharp', 560000, 75, 'NSX009', '2023-09-01', 'Kim loại', 'Sharp', 'LSP02'),
('Q010', 'Quạt Công Nghiệp Daikin', 3600000, 46, 'NSX010', '2023-07-10', 'Thép', 'Daikin', 'LSP05'),
('Q011', 'Quạt Sạc Electrolux', 750000, 67, 'NSX011', '2023-03-23', 'Nhựa', 'Electrolux', 'LSP04'),
('Q012', 'Quạt Bàn Aqua', 410000, 57, 'NSX012', '2023-05-30', 'Nhôm', 'Aqua', 'LSP01'),
('Q013', 'Quạt Đứng Kangaroo', 620000, 34, 'NSX013', '2023-06-06', 'Nhựa', 'Kangaroo', 'LSP01'),
('Q014', 'Quạt Điều Hòa Sanaky', 2400000, 56, 'NSX014', '2023-01-11', 'Kim loại', 'Sanaky', 'LSP03'),
('Q015', 'Quạt Hộp BlueStone', 490000, 40, 'NSX015', '2023-04-22', 'Nhựa', 'BlueStone', 'LSP02');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `su_kien_khuyen_mai`
--

CREATE TABLE `su_kien_khuyen_mai` (
  `MaSKKhuyenMai` varchar(50) NOT NULL,
  `TenKhuyenMai` varchar(200) NOT NULL,
  `PhanTramGiam` int(11) NOT NULL,
  `NgayBatDau` date NOT NULL,
  `NgayKetThuc` date NOT NULL,
  `Loai` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `su_kien_khuyen_mai`
--

INSERT INTO `su_kien_khuyen_mai` (`MaSKKhuyenMai`, `TenKhuyenMai`, `PhanTramGiam`, `NgayBatDau`, `NgayKetThuc`, `Loai`) VALUES
('KM01', 'Giảm giá mùa hè', 20, '2025-06-01', '2025-06-30', 1),
('KM02', 'Khuyến mãi lễ', 15, '2025-04-20', '2025-05-15', 2),
('KM03', 'Siêu sales mùa Đông', 10, '2025-05-06', '2025-05-16', 3);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `taikhoan`
--

CREATE TABLE `taikhoan` (
  `MaTaiKhoan` varchar(10) NOT NULL,
  `TenTaiKhoan` varchar(100) NOT NULL,
  `MatKhau` varchar(100) NOT NULL,
  `VaiTro` varchar(50) NOT NULL,
  `MaNhanVien` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `taikhoan`
--

INSERT INTO `taikhoan` (`MaTaiKhoan`, `TenTaiKhoan`, `MatKhau`, `VaiTro`, `MaNhanVien`) VALUES
('TK000', 'admin', '123', 'Admin', NULL),
('TK001', 'nv1', '123', 'QuanLy', 'NV001'),
('TK002', 'nv2', '123', 'NhanVien', 'NV002');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chitiet_hoadon`
--
ALTER TABLE `chitiet_hoadon`
  ADD PRIMARY KEY (`MaHoaDon`,`MaQuat`),
  ADD KEY `idx_cthd_quat` (`MaQuat`);

--
-- Chỉ mục cho bảng `chitiet_phieunhap`
--
ALTER TABLE `chitiet_phieunhap`
  ADD PRIMARY KEY (`MaPhieuNhap`,`MaQuat`),
  ADD KEY `idx_ctpn_quat` (`MaQuat`);

--
-- Chỉ mục cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`MaHoaDon`),
  ADD KEY `idx_hd_kh` (`MaKhachHang`),
  ADD KEY `idx_hd_nv` (`MaNhanVien`),
  ADD KEY `fk_hd_sk` (`MaSuKienKM`);

--
-- Chỉ mục cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`MaNhanVien`);

--
-- Chỉ mục cho bảng `nha_cung_cap`
--
ALTER TABLE `nha_cung_cap`
  ADD PRIMARY KEY (`MaNCC`);

--
-- Chỉ mục cho bảng `nha_san_xuat`
--
ALTER TABLE `nha_san_xuat`
  ADD PRIMARY KEY (`MaNSX`);

--
-- Chỉ mục cho bảng `phieunhap`
--
ALTER TABLE `phieunhap`
  ADD PRIMARY KEY (`MaPhieuNhap`),
  ADD KEY `idx_pn_ncc` (`MaNCC`),
  ADD KEY `idx_pn_nv` (`MaNhanVien`);

--
-- Chỉ mục cho bảng `quanlibaohanh`
--
ALTER TABLE `quanlibaohanh`
  ADD PRIMARY KEY (`MaBaoHanh`),
  ADD KEY `idx_qbh_quat` (`MaQuat`),
  ADD KEY `idx_qbh_kh` (`MaKhachHang`);

--
-- Chỉ mục cho bảng `quat`
--
ALTER TABLE `quat`
  ADD PRIMARY KEY (`MaQuat`),
  ADD KEY `idx_quat_nsx` (`MaNSX`);

--
-- Chỉ mục cho bảng `su_kien_khuyen_mai`
--
ALTER TABLE `su_kien_khuyen_mai`
  ADD PRIMARY KEY (`MaSKKhuyenMai`);

--
-- Chỉ mục cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD PRIMARY KEY (`MaTaiKhoan`),
  ADD KEY `idx_tk_nv` (`MaNhanVien`);

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chitiet_hoadon`
--
ALTER TABLE chitiet_hoadon
  ADD CONSTRAINT fk_cthd_hd
    FOREIGN KEY (MaHoaDon) REFERENCES hoadon(MaHoaDon)
    ON DELETE CASCADE,
  ADD CONSTRAINT fk_cthd_quat
    FOREIGN KEY (MaQuat) REFERENCES quat(MaQuat);

--
-- Các ràng buộc cho bảng `chitiet_phieunhap`
--
ALTER TABLE `chitiet_phieunhap`
  ADD CONSTRAINT `fk_ctpn_pn` FOREIGN KEY (`MaPhieuNhap`) REFERENCES `phieunhap` (`MaPhieuNhap`),
  ADD CONSTRAINT `fk_ctpn_quat` FOREIGN KEY (`MaQuat`) REFERENCES `quat` (`MaQuat`);

--
-- Các ràng buộc cho bảng `phieunhap`
--
ALTER TABLE `phieunhap`
  ADD CONSTRAINT `fk_pn_ncc` FOREIGN KEY (`MaNCC`) REFERENCES `nha_cung_cap` (`MaNCC`),
  ADD CONSTRAINT `fk_pn_nv` FOREIGN KEY (`MaNhanVien`) REFERENCES `nhanvien` (`MaNhanVien`);

--
-- Các ràng buộc cho bảng `quat`
--
ALTER TABLE `quat`
  ADD CONSTRAINT `fk_quat_nsx` FOREIGN KEY (`MaNSX`) REFERENCES `nha_san_xuat` (`MaNSX`) ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD CONSTRAINT `fk_tk_nv` FOREIGN KEY (`MaNhanVien`) REFERENCES `nhanvien` (`MaNhanVien`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
