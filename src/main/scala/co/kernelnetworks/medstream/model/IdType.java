package co.kernelnetworks.medstream.model;

// The Difference Between a PMCID and a PMID
// The PubMed Central reference number (PMCID) is different from the PubMed reference number (PMID). PubMed Central is
// an index of full-text papers, while PubMed is an index of abstracts. The PMCID links to full-text papers in PubMed
// Central, while the PMID links to abstracts in PubMed. PMIDs have nothing to do with the NIH Public Access Policy.
public enum IdType {
    DOI, // Digital Object Identifier [string]
    PMID, // PMID ID [long]
    PMCID,
    PII, // Personally identifiable information [string] Case insensitive ?
    EID, // Scopus EID [string]
    ClinicalTrials, // ClinicalTrials.gov ID [string]
    IDSNumber, // The Thomson Reuters Document Solution number (used by Web of Science)
    COI, // CAS Object Identifier (ChemPort)
    ManuscriptID, // from a manuscript submission system, e.g., NIHMS, Europe PMC, PMC Canada
    MedlineID,

    ISSN, // The International Standard Serial Number is a unique eight-digit number that identifies periodical publications such as journals and electronic publications
    NLMUniqueID, // AKA LOCATORplusID. Used as magazine ID. the 7-9 digit alpha-numeric identifier used to uniquely identify every record in LocatorPlus. may be 7 to 9 digits in length; the terminal digit may be numeric or the alphabetic characters "A" or "R".
    NLMTA, // Identifier assigned by PubMed, for example, "Mol Biol Cell", "Nucleic Acids Res", etc. This value is typically the journal abbreviation and may be the same as the abbreviated journal title <abbrev-journal-title>.
    Custom // ID specific only to originating content provider
}
