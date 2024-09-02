# Changelog
All notable changes to the Application should be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)

## [7.0.0] - February 2024
### Updates
- Introduced POS Bank net reference number and POS Bank net reference date and TRACE ID in request payload of Transaction Footprint Notification and in the response of View Historical Transaction Footprints.
- Added support for non-card (NC) transactions in Transaction Footprints Calculation API.Introduced new API Delete Payment Card to delete a single payment card.

## [6.0.0] - January 2024
### Updates
- Enriched Transaction Footprints Calculation to support aiia based calculation.

## [5.0.0] - August 2023
### Updates
- Introduced new API Add Payment Cards to register a single and bulk payment card.   

## [4.0.0] - May 2023
### Updates
- Enriched Aggregate Transaction Footprint to support category wise aggregation for transaction of enrolled cards. 

## [3.0.0] - April 2022
### Updates
- Removed the yearly aggregation from the Aggregate Transaction Footprint use case
- Issuer needs to send card brand with each transaction in Transaction Footprints Calculation.

## [2.0.0] - December 2021
### Updates
- Introduced new APIs to leverage the capabilities of Carbon Calculator. 	

## [1.0.0] - March 2021
### Updates
- Launched the Carbon Calculator API to provide visibility into the environmental impact based on the spending habits of consumers.
