-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3308
-- Generation Time: Mar 17, 2023 at 12:34 AM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 7.4.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wechat`
--

-- --------------------------------------------------------

--
-- Table structure for table `groupe`
--

CREATE TABLE `groupe` (
  `Groupe_id` int(11) NOT NULL,
  `Groupe_name` varchar(255) NOT NULL,
  `Groupe_description` varchar(255) DEFAULT NULL,
  `Groupe_admin_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `groupe_messages`
--

CREATE TABLE `groupe_messages` (
  `message_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `GMC_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `groupe_message_content`
--

CREATE TABLE `groupe_message_content` (
  `GMC_id` int(11) NOT NULL,
  `Groupe_id` int(11) NOT NULL,
  `sender_name` varchar(255) NOT NULL,
  `sender_Email` varchar(255) NOT NULL,
  `content` mediumblob NOT NULL,
  `messageType` varchar(10) DEFAULT 'text',
  `fileName` varchar(255) DEFAULT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `message_id` int(11) NOT NULL,
  `sender_Email` varchar(255) NOT NULL,
  `receiver_Email` varchar(255) NOT NULL,
  `message` mediumblob NOT NULL,
  `messageType` varchar(10) DEFAULT 'text',
  `fileName` varchar(255) DEFAULT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`message_id`, `sender_Email`, `receiver_Email`, `message`, `messageType`, `fileName`, `date`) VALUES
(18, 'alice.smith@gmail.com', 'bob.johnson@gmail.com', 0x6869, 'text', NULL, '2023-03-16 18:52:07'),
(19, 'alice.smith@gmail.com', 'bob.johnson@gmail.com', 0x68, 'text', NULL, '2023-03-16 19:03:45');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `password`, `name`) VALUES
(17, 'dalal@gmail.com', '123', 'Dalal Seddoug'),
(18, 'assiya@gmail.com', '123', 'Assiya'),
(19, 'ilias@gmail.com', '123', 'Ilias'),
(20, 'nasrallah@gmail.com', '123', 'Nasrallah');

-- --------------------------------------------------------

--
-- Table structure for table `users_groups`
--

CREATE TABLE `users_groups` (
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `groupe`
--
ALTER TABLE `groupe`
  ADD PRIMARY KEY (`Groupe_id`);

--
-- Indexes for table `groupe_messages`
--
ALTER TABLE `groupe_messages`
  ADD PRIMARY KEY (`message_id`),
  ADD KEY `receiver_id` (`receiver_id`),
  ADD KEY `GMC_id` (`GMC_id`);

--
-- Indexes for table `groupe_message_content`
--
ALTER TABLE `groupe_message_content`
  ADD PRIMARY KEY (`GMC_id`),
  ADD KEY `Groupe_id` (`Groupe_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`message_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `users_groups`
--
ALTER TABLE `users_groups`
  ADD PRIMARY KEY (`user_id`,`group_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `groupe`
--
ALTER TABLE `groupe`
  MODIFY `Groupe_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `groupe_messages`
--
ALTER TABLE `groupe_messages`
  MODIFY `message_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `groupe_message_content`
--
ALTER TABLE `groupe_message_content`
  MODIFY `GMC_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `message_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `groupe_messages`
--
ALTER TABLE `groupe_messages`
  ADD CONSTRAINT `groupe_messages_ibfk_1` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `groupe_messages_ibfk_2` FOREIGN KEY (`GMC_id`) REFERENCES `groupe_message_content` (`GMC_id`) ON DELETE CASCADE;

--
-- Constraints for table `groupe_message_content`
--
ALTER TABLE `groupe_message_content`
  ADD CONSTRAINT `groupe_message_content_ibfk_1` FOREIGN KEY (`Groupe_id`) REFERENCES `groupe` (`Groupe_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
