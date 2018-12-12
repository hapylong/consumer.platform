qq(window).attach("load", function() {
    "use strict";
    var errorHandler = function(id, fileName, reason) {
            return qq.log("id: " + id + ", fileName: " + fileName + ", reason: " + reason);
        },
        azureUploader, s3Uploader, manualUploader, validatingUploader, failingUploader;
    var galleryUploader = new qq.FineUploader({
        element: document.getElementById("manual-example"),
        autoUpload: false,
        template: 'qq-template',
        request: {
            endpoint: document.getElementById("url")
        },
        thumbnails: {
            placeholders: {
                waitingPath: './placeholders/waiting-generic.png',
                notAvailablePath: './placeholders/not_available-generic.png'
            }
        },
        validation: {
            allowedExtensions: ['jpeg', 'jpg', 'gif', 'png']
        }
    });

    qq(document.getElementById("triggerUpload")).attach("click", function() {
        galleryUploader.uploadStoredFiles();
    });

    if (qq.supportedFeatures.ajaxUploading) {
        azureUploader = new qq.azure.FineUploader({
            element: document.getElementById("azure-example"),
            debug: true,
            request: {
                endpoint: "http://fineuploaderdev2.blob.core.windows.net/dev"
            },
            cors: {
                expected: true
            },
            signature: {
                endpoint: "http://192.168.56.101:8080/sas"
            },
            uploadSuccess: {
                endpoint: "http://192.168.56.101:8080/success"
            },
            chunking: {
                enabled: true,
                concurrent: {
                    enabled: true
                }
            },
            resume: {
                enabled: true
            },
            retry: {
                enableAuto: true,
                showButton: true
            },
            deleteFile: {
                enabled: true
            },
            display: {
                fileSizeOnSubmit: true
            },
            paste: {
                targetElement: document
            },
            thumbnails: {
                placeholders: {
                    waitingPath: "./placeholders/waiting-generic.png",
                    notAvailablePath: "./placeholders/not_available-generic.png"
                }
            },
            callbacks: {
                onError: errorHandler,
                onUpload: function (id, filename) {
                    this.setParams({
                        "hey": "hi ɛ $ hmm \\ hi",
                        "ho": "foobar"
                    }, id);

                },
                onStatusChange: function (id, oldS, newS) {
                    qq.log("id: " + id + " " + newS);
                },
                onComplete: function (id, name, response) {
                    qq.log(response);
                }
            }
        });
    }
});