package com.hangman.hangman.repository


fun companyData() = listOf(
    "apple", "intel", "tesla", "cisco", "nike", "honda", "pepsi", "ikea", "visa", "adobe", "linde",
    "dior", "sony", "vale", "cigna", "bayer", "block", "uber", "ford", "gojek", "slack", "vinci",
    "fedex", "lonza", "baidu", "denzo", "nucor", "google", "toyota", "samsung", "amazon", "disney",
    "oracle", "nescafe", "nvidia", "tencent", "walmart", "chevron", "pfizer", "costco", "alibaba",
    "toyota", "verizon", "comcast", "prosus", "netflix", "intuit", "anthem", "paypal", "airbnb",
    "target", "boeing", "seimens", "allianz", "infosys", "shopify", "github", "phonepe", "spotify",
    "twitter", "medkitdoc", "microsoft", "facebook", "gillette", "panasonic", "mastercard", "cocacola",
    "accenture", "novartis", "salesforce", "mcdonald", "qualcomm", "honeywell", "unilever",
    "citigroup", "starbucks", "glencore", "mercedes", "atlassian", "snowflake", "macquarie",
    "linkedin", "soundcloud", "tunnelbear", "wordpress", "whatsapp"
)

fun countryData() = listOf(
    "india", "china", "sudan", "iran", "libya", "chad", "peru", "egypt", "mali", "chile",
    "kenya", "yemen", "spain", "iraq", "japan", "congo", "italy", "oman", "malta", "nauru",
    "ghana", "laos", "syria", "nepal", "cuba", "togo", "haiti", "fiji", "qatar", "tonga",
    "russia", "canada", "brazil", "algeria", "mexico", "angola", "bolivia", "nigeria", "turkey",
    "myanmar", "somalia", "ukraine", "morocco", "germany", "finland", "poland", "vietnam",
    "guinea", "uganda", "romania", "guyana", "belarus", "uruguay", "tunisia", "iceland",
    "jordan", "serbia", "austria", "ireland", "georgia", "panama", "ireland", "croatia",
    "denmark", "bhutan", "taiwan", "latvia", "norway", "france",
    "antarctica", "australia", "argentina", "indonesia", "mongolia", "pakistan", "thailand",
    "cambodia", "bangladesh", "tajikistan", "guatemala", "portugal", "bulgaria", "azerbaijan",
    "lithuania", "palestine", "luxembourg", "dominica", "singapore", "zimbabwe", "botswana",
    "barbados", "maldives", "kiribati", "mauritius", "slovenia", "honduras", "nicaragua",
    "suriname", "kyrgyzstan"
)

fun languageData() = listOf(
    "hindi", "tamil", "urdu", "odia", "dutch", "thai", "igbo", "zulu", "hakka", "greek", "czech",
    "khmer", "rundi", "hausa", "sunda", "spanish", "english", "bengali", "russian", "marathi",
    "telugu", "french", "german", "italian", "kannada", "polish", "yoruba", "burmese", "sindhi",
    "tagalog", "amharic", "magahi", "saraiki", "somali", "cebuano", "nepali", "kazakh", "deccan",
    "uyghur", "arabic", "mandarin", "portuguese", "japanese", "vietnamese", "gujarati", "malayalam",
    "romanian", "chinese", "javanese", "bhojpuri", "indonesian", "maithili", "ukrainian", "assamese",
    "sinhalese", "bavarian", "hungarian"
)

val instructionsList = listOf(
    "1. Game has 5 levels, you will win only if you complete all of those.",
    "2. Points per level will be allotted based on length of the word.",
    "3. You will be having 8 guesses by default for each level, they will reset every level.",
    "4. For each wrong letter you guess, a attempt will be reduced from 8 guesses.",
    "5. For each correct letter you guess, attempt will not be reduced.",
    "6. You can check all your previous game scoring history in history screen.",
    "7. You have 3 types of difficulties to play the game such as easy, medium, hard.",
    "8. You may choose and play game between different categories countries, languages, companies"
)