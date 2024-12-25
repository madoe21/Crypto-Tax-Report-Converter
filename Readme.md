# Crypto Tax Report Converter

This project provides a crypto tax report converter to convert transaction histories to a crypto tax report software format.

Currently supported input transaction history files:

- **[SwissBorg](https://swissborg.com/de/r/martinE4GM)**
- **[Revolut](https://revolut.com/referral/?referral-code=martink4du)**
- **[Kriptomat](https://app.kriptomat.io/ref/join?referral=gyj4yh0j)**

Currently supported crypto tax software:

- **[Blockpit](https://blockpit.cello.so/raqRvglmoPo)**

## Notes

- **Experimental Status:** This project is provided as-is without any warranty. Please thoroughly test the converters before using them in production.
- **FIAT Deposits:** Deposits from FIAT currencies (e.g., EUR, USD) are often not taxable and may need to be manually relabeled as **Non-Taxable (In)** for accurate tax reporting.

### How to get transaction history

#### [SwissBorg](https://swissborg.com/de/r/martinE4GM)

- Transaction history can be generated in the app under **Profile -> Account Statement**.

#### [Revolut](https://revolut.com/referral/?referral-code=martink4du)

- Transaction history can be generated in the app under **Profile -> Documents and Statements -> Crypto -> Account Statement**.

#### [Kriptomat](https://app.kriptomat.io/ref/join?referral=gyj4yh0j)

- Transaction history can be created at **[Kriptomat History](https://app.kriptomat.io/settings/history)**.

## Build project before run

```bash
mvn clean package
```

## How to use

To execute a specific converter, use the following command format:

```bash
mvn exec:java "-Dexec.args=<inputType> <inputFile.csv/inputFile.xlsx> <outputFile.xlsx>"
```

Replace `<inputType>` with the desired type:

- For Revolut transaction history: `revolut`
- For SwissBorg transaction history: `swissborg`
- For Kriptomat transaction history: `kriptomat`

### Example

Run the Revolut Conversion:

```bash
mvn exec:java "-Dexec.args=revolut revolut_input.csv revolut_output.xlsx"
```

Run the SwissBorg Conversion:

```bash
mvn exec:java "-Dexec.args=swissborg swissborg_input.xlsx swissborg_output.xlsx"
```

Run the Kriptomat Conversion:

```bash
mvn exec:java "-Dexec.args=kriptomat kriptomat_input.csv kriptomat_output.xlsx"
```

## Support

I appreciate everyone who supports me and the project! For any requests and suggestions, feel free to provide feedback.

[![Buy Me A Coffee](https://cdn.buymeacoffee.com/buttons/default-orange.png)](https://www.buymeacoffee.com/madoe21)

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
